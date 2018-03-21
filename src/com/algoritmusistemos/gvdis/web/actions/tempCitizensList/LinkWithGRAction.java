package com.algoritmusistemos.gvdis.web.actions.tempCitizensList;

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

public class LinkWithGRAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.LINK_WITH_GR);
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_ASSIGN_GR);
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
