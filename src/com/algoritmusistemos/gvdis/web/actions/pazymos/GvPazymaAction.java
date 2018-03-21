package com.algoritmusistemos.gvdis.web.actions.pazymos;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
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
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GvPazyma;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class GvPazymaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, InternalException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_GV);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_GV_PAZ);
		session.removeAttribute("gvPazyma");
		session.removeAttribute("gvVaikoGvt");
		session.removeAttribute("gvDekData");
		session.removeAttribute("gvVaikai");
		session.removeAttribute("prasymoDataYra");
		long asmNr = 0;
		
		session.setAttribute("prasymoDataYra", "");
		 
		PazymaForm pForm = (PazymaForm)form;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

		request.setAttribute("addressString", "");
		
		
		if (request.getParameter("id") == null) try {	// Bandoma formuoti paþyma
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
			GvPazyma pazyma = new GvPazyma();
			Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, pForm.getAsmKodas());
			asmNr = asmuo.getAsmNr();
			Date data = sdf.parse(pForm.getData());
			GyvenamojiVieta gvt = PazymosDelegator.getInstance().getGvGyvVieta(request, asmuo, data, "R");

			//String addressString = QueryDelegator.getInstance().getAddressString(request, pazyma.getGyvenamojiVieta());
			//if (addressString != null)	request.setAttribute("addressString", addressString);
			
			if ((gvt != null) && ("R".equals(gvt.getGvtTipas()))){					
				String addressString = QueryDelegator.getInstance().getAddressString(request, gvt);
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
				
				if (pazyma.getIstaiga() == null){
					Long istaigaId = (Long)session.getAttribute("userIstaigaId");
					pazyma.setIstaiga(UtilDelegator.getInstance().getIstaiga(istaigaId.longValue(), request));
				}
				
				//Session sess = HibernateUtils.currentSession(request);
				//pazyma.setRegNr(PazymosDelegator.getInstance().getDefaultRegNr(request, sess, "GvPazyma", "PD",false));
				pazyma.setRegNr(""); //Solver: nr formuojamas pries inserta i db
				
				if (gvt != null){
					
					if (pazyma.getDeklaracija()!= null && pazyma.getDeklaracija().getDeklaravimoData()!=null){
						session.setAttribute("gvDekData", pazyma.getDeklaracija().getDeklaravimoData());
					}
					if (gvt.getGvtDataNuo()!= null) {
						session.setAttribute("gvDekData", gvt.getGvtDataNuo());
					}
				}
				else 
					if (null!=pazyma.getDeklaracija() && null != pazyma.getDeklaracija().getDeklaravimoData()){
						session.setAttribute("gvDekData", pazyma.getDeklaracija().getDeklaravimoData());
					}
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
								
								if (vaikoGvt.getGvtDataNuo() == null) {
									iter.remove();
								}
							}
					} catch (Exception e) {}
					session.setAttribute("gvVaikai", vaikai);
					session.setAttribute("gvVaikoGvt", vaikoGvt2);
				}
			}
			session.setAttribute("gvPazyma", pazyma);
		}
		catch (Exception e){
			throw new InternalException(e);
		}
		else try {		// Perþiûrima anksèiau sukurta paþyma
			UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_SKAIT", "RL_GVDIS_SS_SKAIT", "RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
			long id = Long.parseLong(request.getParameter("id"));
			GvPazyma pazyma = PazymosDelegator.getInstance().getGvPazyma2(request, id);
			Asmuo asmuo = pazyma.getGyvenamojiVieta() != null ? pazyma.getGyvenamojiVieta().getAsmuo() : pazyma.getDeklaracija().getAsmuo();
			GyvenamojiVieta gvt2 = pazyma.getGyvenamojiVieta();		

			if (gvt2 != null){
				
				if (pazyma.getDeklaracija()!= null && pazyma.getDeklaracija().getDeklaravimoData()!=null){
					session.setAttribute("gvDekData", pazyma.getDeklaracija().getDeklaravimoData());
				}
				if (gvt2.getGvtDataNuo()!= null) {
					session.setAttribute("gvDekData", gvt2.getGvtDataNuo());
				}
			}
			else 
				if (null!=pazyma.getDeklaracija() && null != pazyma.getDeklaracija().getDeklaravimoData()){
					session.setAttribute("gvDekData", pazyma.getDeklaracija().getDeklaravimoData());
				}
		
			if (pazyma.getPrasymoData() != null) {
				session.setAttribute("prasymoDataYra", "1");
			}
			if (gvt2 != null){
				if (gvt2.getDeklaracija()!=null){
				String pageidaujaPazymos = gvt2.getDeklaracija().getPageidaujaPazymos().toString();
				if (pageidaujaPazymos.equals("1")&& pazyma.getPrasymoRegNr()==null){
			     String deklaracijosNr = gvt2.getDeklaracija().getRegNr(); 	
				 pazyma.setPrasymoRegNr(deklaracijosNr);
				}
			}
			}	
		    if (pazyma.getDeklaracija() != null){
					String pageidaujaPazymos = pazyma.getDeklaracija().getPageidaujaPazymos().toString();
					if (pageidaujaPazymos.equals("1")&& pazyma.getPrasymoRegNr()==null){
					String deklaracijosNr = pazyma.getDeklaracija().getRegNr(); 	
					pazyma.setPrasymoRegNr(deklaracijosNr);
					}
					}
            
			
		    session.setAttribute("gvPazyma", pazyma);
			
			if (asmuo != null){
				asmNr = asmuo.getAsmNr();
                String addressString = QueryDelegator.getInstance().getAddressString(request, pazyma.getGyvenamojiVieta());
				request.setAttribute("addressString", addressString);
                if ((pazyma.getGyvenamojiVieta() != null) && ("R".equals(pazyma.getGyvenamojiVieta().getGvtTipas()))){
					List vaikai = new ArrayList();
					List vaikoGvt2 = new ArrayList();
					if (pazyma.getItrauktiVaikus() == 1){
						GyvenamojiVieta gvt = pazyma.getGyvenamojiVieta();
						vaikai = PazymosDelegator.getInstance().getVaikai(request, gvt.getAsmuo(), pazyma.getPazymosData(), gvt);
						try {
							for (ListIterator iter = vaikai.listIterator(); iter.hasNext();) {
								Asmuo vaikas = (Asmuo) iter.next();								
								GyvenamojiVieta vaikoGvt = (GyvenamojiVieta) vaikas
										.getGyvenamosiosVietos().iterator().next();
								
								if (vaikoGvt.getGvtDataNuo() != null) {
									vaikoGvt2.add(vaikoGvt.getGvtDataNuo());}
								
								if (vaikoGvt.getGvtDataNuo() == null) {
									iter.remove();
								}
							}
						} catch (Exception e) {}
						session.setAttribute("gvVaikai", vaikai);
						session.setAttribute("gvVaikoGvt", vaikoGvt2);
					}
				}
			}
			else if(pazyma.getDeklaracija() != null && pazyma.getDeklaracija().getLaikinasAsmuo() != null){
				Deklaracija dekl = pazyma.getDeklaracija();
				//System.out.println(dekl.getId());
				GyvenamojiVieta gvt = new GyvenamojiVieta();
				Asmuo asm = new Asmuo();
				if(dekl.getLaikinasAsmuo().getPavarde()!= null)asm.setPavarde(dekl.getLaikinasAsmuo().getPavarde());
				if(dekl.getLaikinasAsmuo().getVardas()!= null)asm.setVardas(dekl.getLaikinasAsmuo().getVardas());
				gvt.setAsmuo(asm);
				if (dekl.getTmpGvtAdvNr().intValue()>0){
					gvt.setGvtAdvNr(dekl.getTmpGvtAdvNr());
					System.out.println(dekl.getTmpGvtAdvNr().intValue());
				}
				if (dekl.getTmpGvtAtvNr().intValue()>0){
					gvt.setGvtAtvNr(dekl.getTmpGvtAtvNr());
					System.out.println(dekl.getTmpGvtAtvNr().intValue());
				}
				pazyma.setGyvenamojiVieta(gvt);
				String addressString = QueryDelegator.getInstance().getAddressString(request, pazyma.getGyvenamojiVieta());
				if (addressString != null) request.setAttribute("addressString", addressString);
                if (gvt != null){
					if (null!=pazyma.getDeklaracija() && pazyma.getDeklaracija().getDeklaravimoData()!=null){
						session.setAttribute("gvDekData", pazyma.getDeklaracija().getDeklaravimoData());
					}
					if (gvt.getGvtDataNuo()!= null) {
						session.setAttribute("gvDekData", gvt.getGvtDataNuo());
					}
				}
				else if (null!=pazyma.getDeklaracija() && null != pazyma.getDeklaracija().getDeklaravimoData()){
						session.setAttribute("gvDekData", pazyma.getDeklaracija().getDeklaravimoData());
					}
                
                if (pazyma.getPrasymoData() != null) {
    				session.setAttribute("prasymoDataYra", "1");
    			}
                if (gvt!=null){
                	if (gvt.getDeklaracija()!=null){
                	String pageidaujaPazymos = gvt.getDeklaracija().getPageidaujaPazymos().toString();
    				if (pageidaujaPazymos.equals("1")&& pazyma.getPrasymoRegNr()==null){
    			     String deklaracijosNr = gvt.getDeklaracija().getRegNr(); 	
    				 pazyma.setPrasymoRegNr(deklaracijosNr);
    				}
                   }
                }
    		    if (pazyma.getDeklaracija() != null){
    					String pageidaujaPazymos = pazyma.getDeklaracija().getPageidaujaPazymos().toString();
    					if (pageidaujaPazymos.equals("1")&& pazyma.getPrasymoRegNr()==null){
    					String deklaracijosNr = pazyma.getDeklaracija().getRegNr(); 	
    					pazyma.setPrasymoRegNr(deklaracijosNr);
    					}
    					}
                
                
			}
			if (isToday(pazyma.getPazymosData())) {
				request.setAttribute("gvPazymaSiand", "true");
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
