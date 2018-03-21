package com.algoritmusistemos.gvdis.web.actions.query.copy;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AuditDelegator;
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.QueryPersonForm;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;
import com.algoritmusistemos.gvdis.web.persistence.Asmenvardis;

public class QueryPersonResultsAction extends Action
{

    public QueryPersonResultsAction()
    {
    }

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    	throws DatabaseException, InternalException
    {
        HttpSession session = request.getSession();
        session.setAttribute("CENTER_STATE", "QUERY_PERSON_RESULTS");
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_VIEW_RESIDENCE_DATA);
        QueryPersonForm qForm = (QueryPersonForm)form;
        
        String reasonCode = qForm.getPriezKodas();
        String reasonExpl = qForm.getPriezAprasymas();
        String code = qForm.getAsmKodas();
 
       
        // Audituojame bandymà paþiûrëti duomenis
        long auditId = 0;
        if (!"".equals(reasonCode)){
        	auditId = AuditDelegator.getInstance().auditQueryByCode(request, code, reasonCode, reasonExpl);
        	session.setAttribute("lastAuditId", new Long(auditId));
        }
        else if (session.getAttribute("lastAuditId") == null){
    		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.");
        }
        else try {
        	auditId = ((Long)session.getAttribute("lastAuditId")).longValue();
        }
        catch (Exception e){
        	throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.");
        }

        auditId = ((Long)session.getAttribute("lastAuditId")).longValue();
        
        try {
        	// Paimame asmená 
        	Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByCode(request, code);
        	request.setAttribute("asmuo", asmuo);
        	
        	// Audituojame asmens duomenø perþiûros faktà
        	AuditDelegator.getInstance().auditPersonResult(request, auditId, asmuo);
        	
        	List gyvenamosiosVietos = QueryDelegator.getInstance().getGyvenamosiosVietos(request,asmuo.getAsmNr());
        	
        	//rikuojam taip bo hibernatas nulls last nepalaiko
        	Collections.sort(gyvenamosiosVietos, new Comparator(){
				public int compare(Object arg0, Object arg1) {
					int rez = 0;
					
					if(((GyvenamojiVieta) arg0).getGvtDataNuo() == null)
						return 1;
					switch (((GyvenamojiVieta) arg0).getGvtDataNuo().compareTo(
							((GyvenamojiVieta) arg1).getGvtDataNuo())) {
					case -1: rez = 1; break;
					case 1:  rez = -1; break;
					}
					return rez;
				}});     
        	
        	for (Iterator it=gyvenamosiosVietos.iterator(); it.hasNext(); ){
        		GyvenamojiVieta vt = (GyvenamojiVieta)it.next();     		
        		if (vt.getGvtDataIki() == null){
        			it.remove();
        			Asmenvardis asmenvardis = new Asmenvardis();
        			if (vt.getDeklaracija() != null && vt.getDeklaracija().getAsmenvardis() != null) asmenvardis = vt.getDeklaracija().getAsmenvardis();
        			request.setAttribute("aktAsmenvardis", asmenvardis);
        			request.setAttribute("aktVieta", vt);
        			break;
        		}
        	}

        	//        	request.setAttribute("deklaracijos", deklaracijos.size() > 0 ? deklaracijos : null);
        	request.setAttribute("gyvenamosiosVietos", gyvenamosiosVietos.size() > 0 ? gyvenamosiosVietos : null);
        	
        
//        	List gyvenamosiosVietos = QueryDelegator.getInstance().getGyvenamosiosVietos(request,asmuo.getAsmNr());
//        	List gyvenamosiosVietosCopy = new ArrayList();
//        	Collections.copy(gyvenamosiosVietos, gyvenamosiosVietosCopy);
//
//        	Collections.sort(gyvenamosiosVietosCopy, new Comparator(){
//				public int compare(Object arg0, Object arg1) {
//					int rez = 0;
//					if(((GyvenamojiVieta) arg0).getGvtDataNuo() == null)
//						return 1;
//					switch (((GyvenamojiVieta) arg0).getGvtDataNuo().compareTo(
//							((GyvenamojiVieta) arg1).getGvtDataNuo())) {
//							case -1: rez = 1; break;
//							case 1:  rez = -1; break;
//					}
//					return rez;
//				}});     
//        	
//        	GyvenamojiVieta vtFound = null;        	
//        	for (Iterator it=gyvenamosiosVietosCopy.iterator(); it.hasNext(); ){
//        		GyvenamojiVieta vt = (GyvenamojiVieta)it.next(); 
//        		if (vt.getGvtDataIki() == null){
//        			it.remove();
//        			vtFound = vt;
//        			break;
//        		} 
//        		  else if (vt.getGvtDataIki() != null && inRange(vt.getGvtTipas())) {
//        			it.remove();
//        			vtFound = vt;
//        			break;
//        		}
//        	}
//        	
//        	
//        	request.setAttribute("aktVieta", vtFound);
//        	request.setAttribute("gyvenamosiosVietos", gyvenamosiosVietos.size() > 0 ? gyvenamosiosVietos : null);
        }
        catch (ObjectNotFoundException onfe){
        	request.setAttribute("errorMessage", onfe.getMessage());
        }
        
        return mapping.findForward("continue");
    }
    
    public boolean inRange(String str) {
    	if (str.equals("C") || 
    		str.equals("D") ||
    		str.equals("G") ||
    		str.equals("H") ||
    		str.equals("I") ||
    		str.equals("O") ||
    		str.equals("S") ||
    		str.equals("Z")) {
    		return true;    
    	} else {
    		return false;
    	}
    } 
}
