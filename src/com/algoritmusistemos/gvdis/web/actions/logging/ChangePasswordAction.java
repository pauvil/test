package com.algoritmusistemos.gvdis.web.actions.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.forms.ChangePasswordForm;

public class ChangePasswordAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException
	{
		HttpSession session = request.getSession();
		ChangePasswordForm chPassForm = (ChangePasswordForm)form;
		UserDelegator.getInstance().changePassword(request, chPassForm.getNewPassword());
		session.setAttribute("userPassword", chPassForm.getNewPassword());
    	return (mapping.findForward(Constants.CONTINUE));
	}
}
