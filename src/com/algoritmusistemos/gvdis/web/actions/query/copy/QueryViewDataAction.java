package com.algoritmusistemos.gvdis.web.actions.query.copy;

import java.util.Map;
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
import com.algoritmusistemos.gvdis.web.delegators.QueryDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvnaDeklaracija;
import com.algoritmusistemos.gvdis.web.persistence.GyvenamojiVieta;

public class QueryViewDataAction extends Action
{
    public QueryViewDataAction()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ObjectNotFoundException, InternalException, DatabaseException
    {
        HttpSession session = request.getSession();
        session.setAttribute("CENTER_STATE", "QUERY_VIEW_DATA");
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_VIEW_RESIDENCE_DATA);
        
        try {
        	long asmNr = Long.parseLong(request.getParameter("gvtAsmNr"));
        	long gvtNr = Long.parseLong(request.getParameter("gvtNr"));
        	Asmuo asmuo = QueryDelegator.getInstance().getAsmuoByAsmNr(request, asmNr);
        	
        	GyvenamojiVieta gyvenamojiVieta = QueryDelegator.getInstance().getGyvenamojiVieta(request, asmNr, gvtNr);

        	if (gyvenamojiVieta.getDeklaracija() != null){
        		asmuo.setVardasPavarde(gyvenamojiVieta.getDeklaracija().getAsmenvardis());
        	} else {
        		/*if (gyvenamojiVieta.getSprendimas() != null) {
        			asmuo.setVardasPavarde(request, gyvenamojiVieta.getSprendimas().getData());
        		} else {
            		asmuo.setVardasPavarde(request, gyvenamojiVieta.getGvtDataNuo());
        		}*/
        	}
        	request.setAttribute("asmuo", asmuo);
        	
        	request.setAttribute("gyvenamojiVieta", gyvenamojiVieta);
        	
        	if("K".equals(gyvenamojiVieta.getGvtTipas()))
        	{
            	try{
            		if (null != gyvenamojiVieta.getGvtAtvNr()){
            			String savivaldybe;
            			savivaldybe = AdresaiDelegator.getInstance().getTerVieneta(gyvenamojiVieta.getGvtAtvNr(), request).getTerPav();
            			request.setAttribute("savivaldybe",savivaldybe);
            		}
            		else {
            			GvnaDeklaracija gvna = (GvnaDeklaracija)gyvenamojiVieta.getDeklaracija();
            			request.setAttribute("savivaldybe",gvna.getSavivaldybe().getTerPav());
            		}
            	}
            	catch(ClassCastException cce){cce.printStackTrace();}
            	catch(NullPointerException npe){npe.printStackTrace();}
        	}
        	
        	Map flatAddress = QueryDelegator.getInstance().getFlatAddress(request, gyvenamojiVieta);
        	request.setAttribute("flatAddress", flatAddress);
        	
       	
        	//String stringAddress = QueryDelegator.getInstance().getAddressString(request, gyvenamojiVieta);
        	//if (gyvenamojiVieta.getSprendimas() != null) {
	        	//if ((gyvenamojiVieta.getSprendimas().getTipas() < 2)) {
	        		String stringAddress = AdresaiDelegator.getInstance().getAsmAddress(asmuo.getAsmNr(),gyvenamojiVieta.getGvtNr(),request);
	        		request.setAttribute("stringAddress", stringAddress);
	        	//}
        	//}
        	/*else {
        		String stringAddress = AdresaiDelegator.getInstance().getAsmAddress(asmuo.getAsmNr(),gyvenamojiVieta.getGvtNr(),request);
        		request.setAttribute("stringAddress", stringAddress);        		
        	}*/
        	
        	try {
        		long auditId = ((Long)session.getAttribute("lastAuditId")).longValue();
        		AuditDelegator.getInstance().auditGvtResult(request, auditId, gyvenamojiVieta);
        	}
        	catch (Exception nfe){
        		throw new InternalException("Neuþregistruota paieðkos uþklausa auditui. Griþkite á paieðkos formà ir pabandykite dar kartà.", nfe);
        	}
        }
        catch (NumberFormatException nfe){
        	throw new InternalException("Perduoti neteisingi parametrai", nfe);
        }
        
        return mapping.findForward("continue");
    }
}
