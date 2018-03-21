package com.algoritmusistemos.gvdis.web.actions.pazymos;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AdresaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.forms.PazymaForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;

public class IsvykePazymaAction extends Action {

	public ActionForward execute(ActionMapping mapper, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws Exception 
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_ISVYKE);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_ISVYKE_PAZ);
		session.removeAttribute("isvykePazyma");
		session.removeAttribute("isvykeVaikai");
		long asmNr = 0;
		
		PazymaForm pForm = (PazymaForm)form;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		
		if (request.getParameter("id") == null) try {	// Bandoma formuoti paþyma
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
			GvPazyma pazyma = new GvPazyma();
			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, pForm.getAsmKodas());
			asmNr = asmuo.getAsmNr();
			Date data = sdf.parse(pForm.getData());
			GyvenamojiVieta gvt = PazymosDelegator.getInstance().getGvGyvVieta(request, asmuo, data, "V");
			if ((gvt != null) && ("V".equals(gvt.getGvtTipas()))){					
				//String addressString = QueryDelegator.getInstance().getAddressString(request, gvt);
				String addressString = AdresaiDelegator.getInstance().getAsmAddress(gvt.getGvtAsmNr(), gvt.getGvtNr(), request);
				request.setAttribute("addressString", addressString);
				boolean itrauktiVaikus = "on".equals(pForm.getVaikai());
			
				pazyma.setPazymosData(new Timestamp(data.getTime()));
				pazyma.setPastabos(pForm.getPastabos());
				pazyma.setPrasymoRegNr(pForm.getPrasymoRegNr());
				pazyma.setItrauktiVaikus(itrauktiVaikus ? 1 : 0);
				pazyma.setGyvenamojiVieta(gvt);
				try {
					pazyma.setPrasymoData(new Timestamp(sdf.parse(pForm.getPrasymoData()).getTime()));
				}
				catch (Exception e){
					pazyma.setPrasymoData(null);
				}
				
				if (itrauktiVaikus){
					List vaikai = PazymosDelegator.getInstance().getVaikai(request, asmuo, data, gvt);
					session.setAttribute("isvykeVaikai", vaikai);
				}
			}
			session.setAttribute("isvykePazyma", pazyma);
		}
		catch (Exception e){
			throw new InternalException(e);
		}
		else try {		// Perþiûrima anksèiau sukurta paþyma
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_SKAIT", "RL_GVDIS_SS_SKAIT", "RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
			long id = Long.parseLong(request.getParameter("id"));
			GvPazyma pazyma = PazymosDelegator.getInstance().getGvPazyma(request, id);
			Asmuo asmuo = pazyma.getGyvenamojiVieta() != null ? pazyma.getGyvenamojiVieta().getAsmuo() : pazyma.getDeklaracija().getAsmuo();

			session.setAttribute("isvykePazyma", pazyma);			
			if (asmuo != null){
				asmNr = asmuo.getAsmNr();
				if ((pazyma.getGyvenamojiVieta() != null) && ("V".equals(pazyma.getGyvenamojiVieta().getGvtTipas()))){
					//String addressString = QueryDelegator.getInstance().getAddressString(request, pazyma.getGyvenamojiVieta());
					String addressString = AdresaiDelegator.getInstance().getAsmAddress(pazyma.getGyvenamojiVieta().getGvtAsmNr(), pazyma.getGyvenamojiVieta().getGvtNr(), request);
					request.setAttribute("addressString", addressString);
					List vaikai = new ArrayList();
					if (pazyma.getItrauktiVaikus() == 1){
						GyvenamojiVieta gvt = pazyma.getGyvenamojiVieta();
						vaikai = PazymosDelegator.getInstance().getVaikai(request, gvt.getAsmuo(), pazyma.getPazymosData(), gvt);
					}
					session.setAttribute("isvykeVaikai", vaikai);
				}
			}
		}
		catch (Exception e){
			throw new InternalException(e);
		}
		
		// Uþfiksuojame asmens duomenø perþiûros faktà auditui
    	if (asmNr > 0) try {
    		Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmNr);
    		long auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(asmuo.getAsmKodas()), Constants.AUDIT_GV_PAZ_PERZIURA, "GV paþymos perþiûra");
    		AuditDelegator.getInstance().auditPersonResult(request, auditId, asmuo);
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
    	}

		return mapper.findForward(Constants.CONTINUE);
	}

}
