package com.algoritmusistemos.gvdis.web.actions.pazymos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.PazymaForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvnaPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class GvnaPazymaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_GVNA);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_GVNA_PAZ);
		session.removeAttribute("gvnaPazyma");
		session.removeAttribute("gvnaVaikai");
		session.removeAttribute("gvnaGvtDataNuo");
		session.removeAttribute("gvnaSavivaldybe");
		session.removeAttribute("gvnaVaikoGvt");
		session.removeAttribute("prasymoDataYra");
		PazymaForm pForm = (PazymaForm)form;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		long asmNr = 0;

		session.setAttribute("prasymoDataYra", "");
		
		if (request.getParameter("id") == null) try {	// Bandoma formuoti paþyma
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
			GvnaPazyma pazyma = new GvnaPazyma();
			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, pForm.getAsmKodas());
			asmNr = asmuo.getAsmNr();
			Date data = sdf.parse(pForm.getData());
			GyvenamojiVieta gvt = PazymosDelegator.getInstance().getGvnaGyvVieta(request, asmuo, data);
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
			if (pazyma.getIstaiga() == null){
				Long istaigaId = (Long)session.getAttribute("userIstaigaId");
				pazyma.setIstaiga(UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(), request));
			}
			
			//Session sess = HibernateUtils.currentSession(request);
			//pazyma.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, sess, "GvnaPazyma","PN",false));
			pazyma.setRegNr(""); //Solver: nr formuojamas pries inserta i db
			
			GyvenamojiVieta gvt2=pazyma.getGyvenamojiVieta();
			String savivaldybe = new String();
				if (gvt2!=null){
					savivaldybe = QueryDelegator.getInstance().getAddressString(request, gvt2);
					session.setAttribute("gvnaSavivaldybe",savivaldybe);
					session.setAttribute("gvnaGvtDataNuo",gvt2.getGvtDataNuo());
				}
				else if (pazyma.getDeklaracija()!=null && pazyma.getDeklaracija().getLaikinasAsmuo() != null){
					savivaldybe=pazyma.getDeklaracija().getSavivaldybe().getTerPav();
					session.setAttribute("gvnaSavivaldybe",savivaldybe);	
					session.setAttribute("gvnaGvtDataNuo",pazyma.getDeklaracija().getDeklaravimoData());	
				}
			    
/*				if (gvt2 != null){
					session.setAttribute("gvnaGvtDataNuo",gvt2.getGvtDataNuo());
				}
			    else if (pazyma.getDeklaracija()!=null && pazyma.getDeklaracija().getLaikinasAsmuo() == null){
				    session.setAttribute("gvnaGvtDataNuo","");	
				    }
				else if (pazyma.getDeklaracija()!=null && pazyma.getDeklaracija().getLaikinasAsmuo() != null){
				session.setAttribute("gvnaGvtDataNuo",pazyma.getDeklaracija().getDeklaravimoData());	
				}
*/				
			session.setAttribute("gvnaPazyma", pazyma);
			
			if (itrauktiVaikus){
				List vaikai = PazymosDelegator.getInstance().getVaikai(request, asmuo, data, gvt);
				List vaikoGvt2 = new ArrayList();
				try {
					for (ListIterator iter = vaikai.listIterator(); iter.hasNext();) {
						Asmuo vaikas = (Asmuo) iter.next();	
						GyvenamojiVieta vaikoGvt = (GyvenamojiVieta) vaikas
								.getGyvenamosiosVietos().iterator().next();
						if (vaikoGvt.getGvtDataNuo() != null) {
							vaikoGvt2.add(vaikoGvt.getGvtDataNuo());}
						}
				} catch (Exception e) {}
				session.setAttribute("gvnaVaikoGvt", vaikoGvt2);	
				session.setAttribute("gvnaVaikai", vaikai);
			}
		}
		catch (Exception e){
			throw new InternalException(e);
		}
		else try {		// Perþiûrima anksèiau sukurta paþyma
			//UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_SKAIT", "RL_GVDIS_SS_SKAIT"}); kom ju.k 2007.06.28
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_SKAIT", "RL_GVDIS_SS_SKAIT", "RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"}); //ju.k 2007.06.28
			long id = Long.parseLong(request.getParameter("id"));
			GvnaPazyma pazyma = PazymosDelegator.getInstance().getGvnaPazyma(request, id);
			session.setAttribute("gvnaPazyma", pazyma);
			Asmuo asmuo = pazyma.getGyvenamojiVieta() != null ? pazyma.getGyvenamojiVieta().getAsmuo() : pazyma.getDeklaracija().getAsmuo();
		    GyvenamojiVieta gvt2=pazyma.getGyvenamojiVieta();
			String savivaldybe = new String();
			if (pazyma.getPrasymoData() != null) {
				session.setAttribute("prasymoDataYra", "1");
			}
			
			if (gvt2!=null){
				savivaldybe = QueryDelegator.getInstance().getAddressString(request, gvt2);
				session.setAttribute("gvnaSavivaldybe",savivaldybe);
		    	session.setAttribute("gvnaGvtDataNuo",gvt2.getGvtDataNuo());
		    	if (pazyma.getGyvenamojiVieta().getDeklaracija()!=null){
		    	String pageidaujaPazymos = pazyma.getGyvenamojiVieta().getDeklaracija().getPageidaujaPazymos().toString();
				if (pageidaujaPazymos.equals("1") && pazyma.getPrasymoRegNr()==null){
				String deklaracijosNr = pazyma.getGyvenamojiVieta().getDeklaracija().getRegNr(); 	
				pazyma.setPrasymoRegNr(deklaracijosNr);
				}
		    	}
			}
			else if (pazyma.getDeklaracija()!=null && pazyma.getDeklaracija().getLaikinasAsmuo() != null){
				savivaldybe=pazyma.getDeklaracija().getSavivaldybe().getTerPav();
				session.setAttribute("gvnaSavivaldybe",savivaldybe);	
				session.setAttribute("gvnaGvtDataNuo",pazyma.getDeklaracija().getDeklaravimoData());	
			}
			
			//System.out.println(pazyma.getDeklaracija().getPageidaujaPazymos());
			if (pazyma.getDeklaracija() != null){
				String pageidaujaPazymos = pazyma.getDeklaracija().getPageidaujaPazymos().toString();
				if (pageidaujaPazymos.equals("1") && pazyma.getPrasymoRegNr()==null){
				String deklaracijosNr = pazyma.getDeklaracija().getRegNr(); 	
				pazyma.setPrasymoRegNr(deklaracijosNr);
				}
				}
			
/*		    if (gvt2 != null){
		    	session.setAttribute("gvnaGvtDataNuo",gvt2.getGvtDataNuo());
			}
		    else if (pazyma.getDeklaracija()!=null && pazyma.getDeklaracija().getLaikinasAsmuo() == null){
		    	session.setAttribute("gvnaGvtDataNuo","");	
		    }
			else if (pazyma.getDeklaracija()!=null && pazyma.getDeklaracija().getLaikinasAsmuo() != null){
				session.setAttribute("gvnaGvtDataNuo",pazyma.getDeklaracija().getDeklaravimoData());	
			}
*/		    
			if (asmuo != null){
				asmNr = asmuo.getAsmNr();
				List vaikai = new ArrayList();
				List vaikoGvt2 = new ArrayList();
				if (pazyma.getItrauktiVaikus() == 1){
					GyvenamojiVieta gvt = pazyma.getGyvenamojiVieta();
					vaikai = PazymosDelegator.getInstance().getVaikai(request, asmuo, pazyma.getPazymosData(), gvt);
					try {
						for (ListIterator iter = vaikai.listIterator(); iter.hasNext();) {
							Asmuo vaikas = (Asmuo) iter.next();								
							GyvenamojiVieta vaikoGvt = (GyvenamojiVieta) vaikas
									.getGyvenamosiosVietos().iterator().next();
							if (vaikoGvt.getGvtDataNuo() != null) {
								vaikoGvt2.add(vaikoGvt.getGvtDataNuo());}
						}
				
					} catch (Exception e) {}
					session.setAttribute("gvnaVaikoGvt", vaikoGvt2);
					session.setAttribute("gvnaVaikai", vaikai);
				}
			}
			if (isToday(pazyma.getPazymosData())) {
				request.setAttribute("gvnaPazymaSiand", "true");
			}
		}
		catch (Exception e){
			throw new InternalException(e);
		}
		
		// Uþfiksuojame asmens duomenø perþiûros faktà auditui
    	if (asmNr > 0) try {
    		Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmNr);
    		long auditId = AuditDelegator.getInstance().auditQueryByCode(request, String.valueOf(asmuo.getAsmKodas()), Constants.AUDIT_GVNA_PAZ_PERZIURA, "GVNA paþymos perþiûra");
    		AuditDelegator.getInstance().auditPersonResult(request, auditId, asmuo);
    	}
    	catch (Exception nfe){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
    	}
		
	    return mapping.findForward(Constants.CONTINUE);
	}
	 private boolean isToday(Date date) {
		 Calendar today = Calendar.getInstance();
		 today.setTime(new Date());

		 Calendar otherday = Calendar.getInstance();
		 otherday.setTime(date);

		 return otherday.get(Calendar.YEAR) == today.get(Calendar.YEAR)
		 && otherday.get(Calendar.MONTH) == today.get(Calendar.MONTH)
		 && otherday.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
		 }
}
