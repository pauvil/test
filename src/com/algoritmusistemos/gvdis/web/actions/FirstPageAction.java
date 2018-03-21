package com.algoritmusistemos.gvdis.web.actions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;

public class FirstPageAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, ObjectNotFoundException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, "");
	    return (mapping.findForward(Constants.CONTINUE));
	}
}