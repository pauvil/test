package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

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

public class AfterIsvDeklaracijaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
	    HttpSession session = request.getSession();
		String state = String.valueOf(session.getAttribute(Constants.CENTER_STATE));
		if (!state.equals(Constants.CHNG_OUT_DECLARATION_FORM)) { 
			session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_FORM_FILL_MESSAGE);
		}
		else {
			session.setAttribute(Constants.CENTER_STATE, Constants.CHNG_OUT_DECLARATION_FORM_FILL_MESSAGE);
		}
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);	    
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
