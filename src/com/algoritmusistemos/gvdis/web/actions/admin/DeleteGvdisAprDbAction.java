package com.algoritmusistemos.gvdis.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AprasymaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;

public class DeleteGvdisAprDbAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException, PermissionDeniedException
	{
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
		long id = Long.parseLong(request.getParameter("id"));

		AprasymaiDelegator.getInstance().deleteDbObjectDescription(request, id);
		
		return (mapping.findForward(Constants.CONTINUE));
	}

	
}
