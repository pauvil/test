package com.algoritmusistemos.gvdis.web.actions.pazymos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;

public class MirePazymaFormAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws ObjectNotFoundException, PermissionDeniedException 
	{
		HttpSession session = request.getSession();
        session.setAttribute("menu_cell","mirepazymaform");	
	    session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_MIRE_FORM);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_MIRE_PAZ);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		
		session.removeAttribute("mirePazyma");
		
		return mapping.findForward(Constants.CONTINUE);
	}

}
