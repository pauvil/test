package com.algoritmusistemos.gvdis.web.actions.tempCitizensList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;

public class DeleteTempCitizenAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
		long l = 0;
		try{
		l = Long.parseLong(request.getParameter("id"));
		}
		catch(NumberFormatException nfe)
		{
			throw new ObjectNotFoundException("LaikinasAsmuo with id ["+request.getParameter("id")+"] not found", nfe);
		}
	    if(-1 == UserDelegator.getInstance().DeleteTempCitizen(l,request))
    	{
    	    request.getSession().setAttribute(Constants.CENTER_STATE, Constants.CANNOT_DELETE_TEMP_CITIZEN);	    		
    	    return (mapping.findForward(Constants.CANNOT_DELETE_TEMP_CITIZEN));
    	}
	    return (mapping.findForward(Constants.CONTINUE));
	}
}