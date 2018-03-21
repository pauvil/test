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
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;

public class MirePazymaAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws Exception 
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_MIRE);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_MIRE_PAZ);
		session.removeAttribute("mirePazyma");
		session.removeAttribute("mireVaikai");
		long asmNr = 0;
		
		PazymaForm pForm = (PazymaForm)form;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		
		if (request.getParameter("id") == null) try {	// Bandoma formuoti paþyma
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
			GvPazyma pazyma = new GvPazyma();
			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, pForm.getAsmKodas());
			asmNr = asmuo.getAsmNr();
			Date data = sdf.parse(pForm.getData());
			GyvenamojiVieta gvt = PazymosDelegator.getInstance().getMireGyvVieta(request, asmuo);
			if (gvt != null){					
				//String addressString = QueryDelegator.getInstance().getAddressString(request, gvt);
				
				String addressString =AdresaiDelegator.getInstance().getAsmAddress2(asmuo.getAsmNr(),gvt.getGvtNr(),request);
				/*String tempString = "";
				if(!gvt.getGvtDataIki().equals(asmuo.getAsmMirtiesData())){
					tempString += "Paskutinë gyvenamoji vieta buvo deklaruota adresu: " + addressString
								+ " nuo " + sdf.format(gvt.getGvtDataNuo()) + " iki " + sdf.format(gvt.getGvtDataIki())
								+ "<br/>" + "";
				}*/
				addressString = (addressString == null)? "":addressString;				
				
				if("K".equals(gvt.getGvtTipas()))
	        	{
	            	try{
	            		if (null != gvt.getGvtAtvNr()){
	            			String savivaldybe;
	            			savivaldybe = AdresaiDelegator.getInstance().getTerVieneta(gvt.getGvtAtvNr(), request).getTerPav();
	            			addressString += " " + savivaldybe;
	            		}
	            		else {
	            			GvnaDeklaracija gvna = (GvnaDeklaracija)gvt.getDeklaracija();
	            			addressString += " " + gvna.getSavivaldybe().getTerPav();
	            		}
	            	}
	            	catch(ClassCastException cce){cce.printStackTrace();}
	            	catch(NullPointerException npe){npe.printStackTrace();}
	        	}
				
				request.setAttribute("addressString", (addressString == null)? "":addressString);
				
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
					session.setAttribute("mireVaikai", vaikai);
				}
			}
			session.setAttribute("mirePazyma", pazyma);
		}
		catch (Exception e){
			throw new InternalException(e);
		}
		else try {		// Perþiûrima anksèiau sukurta paþyma
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_SKAIT", "RL_GVDIS_SS_SKAIT", "RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
			long id = Long.parseLong(request.getParameter("id"));
			GvPazyma pazyma = PazymosDelegator.getInstance().getGvPazyma(request, id);
			Asmuo asmuo = pazyma.getGyvenamojiVieta() != null ? pazyma.getGyvenamojiVieta().getAsmuo() : pazyma.getDeklaracija().getAsmuo();

			session.setAttribute("mirePazyma", pazyma);			
			if (asmuo != null){
				asmNr = asmuo.getAsmNr();
				if ((pazyma.getGyvenamojiVieta() != null) ){
					//String addressString = QueryDelegator.getInstance().getAddressString(request, pazyma.getGyvenamojiVieta());
					String addressString =AdresaiDelegator.getInstance().getAsmAddress2(asmuo.getAsmNr(),pazyma.getGyvenamojiVieta().getGvtNr(),request);
					GyvenamojiVieta gvtTemp = pazyma.getGyvenamojiVieta();
					addressString = (addressString == null)? "":addressString;				
					
					if("K".equals(gvtTemp.getGvtTipas()))
		        	{
		            	try{
		            		if (null != gvtTemp.getGvtAtvNr()){
		            			String savivaldybe;
		            			savivaldybe = AdresaiDelegator.getInstance().getTerVieneta(gvtTemp.getGvtAtvNr(), request).getTerPav();
		            			addressString += " " + savivaldybe;
		            		}
		            		else {
		            			GvnaDeklaracija gvna = (GvnaDeklaracija)gvtTemp.getDeklaracija();
		            			addressString += " " + gvna.getSavivaldybe().getTerPav();
		            		}
		            	}
		            	catch(ClassCastException cce){cce.printStackTrace();}
		            	catch(NullPointerException npe){npe.printStackTrace();}
		        	}
					
					request.setAttribute("addressString", (addressString == null)? "":addressString);
					List vaikai = new ArrayList();
					if (pazyma.getItrauktiVaikus() == 1){
						GyvenamojiVieta gvt = pazyma.getGyvenamojiVieta();
						vaikai = PazymosDelegator.getInstance().getVaikai(request, gvt.getAsmuo(), pazyma.getPazymosData(), gvt);
					}
					session.setAttribute("mireVaikai", vaikai);
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

    	return mapping.findForward(Constants.CONTINUE);
	}

}
