package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;

public class ViewPrasymasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InternalException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.SPR_VIEW_PRASYMAS);
	    
	    try {
	    	Long id = Long.valueOf((String)request.getParameter("id"));
	    	PrasymasKeistiDuomenis prasymas = SprendimaiDelegator.getInstance().getPrasymas(request, id.longValue());
	    	
	    	//Tieto Paraiska Nr. 7;
	    	//if (prasymas.getTipasStr().equals("Naikinti duomenis") 
	    	//	|| prasymas.getTipasStr().equals("Naikinti GVNA duomenis")) {
	    	//	prasymas.setNaujasAdresas(null);	    			    		
	    	//}
	    		    	
	    	
	    	request.setAttribute("prasymas", prasymas);
	    	prasymas.getInsDate(); // gali jau buti istrintas
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	    	throw new InternalException("Blogi praðymo parametrai", e);
	    }
    	
    	return (mapping.findForward(Constants.CONTINUE));
	}
}
