package com.algoritmusistemos.gvdis.web.actions.tempCitizensList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.utils.Ordering;

public class TempCitizensListAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException
	{
		HttpSession session = request.getSession();
        session.setAttribute("menu_cell","TempCitizensList");
		session.setAttribute(Constants.CENTER_STATE, Constants.TEMP_CITIZENS);
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_ASSIGN_GR);
        
        
        //
//      Parametrø paëmimas 
	    Long savivaldybe = null;
	    Long seniunija = null;
	    
	    int userStatus = ((Integer)session.getAttribute("userStatus")).intValue();
	    try { 
	    	switch (userStatus){
	    	case UserDelegator.USER_GLOBAL: 
	    		savivaldybe = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	case UserDelegator.USER_SAV:
	    		savivaldybe = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	default: savivaldybe = null;
	    	}
	    } 
	    catch (NumberFormatException ignore){}
	    
	    try {  
	    	switch (userStatus){
	    	case UserDelegator.USER_SEN:
	    		seniunija = (Long)session.getAttribute("userIstaigaId");
	    		break;
	    	}
	    } 
	    catch (NumberFormatException ignore){}
        
        //
        
        Ordering ordering = (Ordering)session.getAttribute("temp_citizens_ordering");
        if (ordering == null){
            ordering = new Ordering();
        }
        if (ordering.getColumn() == null) ordering.setColumn("insDate"); // Pagal nutylëjimà
        ordering.init(request);	
        session.setAttribute("temp_citizens_ordering", ordering);
		request.setAttribute("tempCitizens",DeklaracijosDelegator.getInstance(request).getTempCitizens(request,ordering,savivaldybe,seniunija));
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
