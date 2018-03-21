package com.algoritmusistemos.gvdis.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AprasymaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.AprModulis;


public class CreateGvdisAprasymasFormAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws InternalException, PermissionDeniedException
		{
		    HttpSession session = request.getSession();
		    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_GVDIS_APRASYMAS_CREATE);
			UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		    
		    try {
		    	long modulioId = Long.parseLong(request.getParameter("modulioId"));
		    	AprModulis mod = AprasymaiDelegator.getInstance().getAprModulis(request, modulioId);
		    	request.setAttribute("aprmodulis", mod);
		    }
		    catch (Exception e){
		    	throw new InternalException("Blogas ID", e);
		    }

		    return (mapping.findForward(Constants.CONTINUE));
		}


}
