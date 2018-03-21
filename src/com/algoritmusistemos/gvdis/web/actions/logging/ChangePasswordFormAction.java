package com.algoritmusistemos.gvdis.web.actions.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;

public class ChangePasswordFormAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.CHANGE_PASSWORD_FORM);
	    
    	return (mapping.findForward(Constants.CONTINUE));
	}
}
