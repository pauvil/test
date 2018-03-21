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

public class IsvykePazymaFormAction extends Action {

	public ActionForward execute(ActionMapping mapper, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws Exception 
	{
		HttpSession session = request.getSession();
        session.setAttribute("menu_cell","isvykepazymaform");	
	    session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_ISVYKE_FORM);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_ISVYKE_PAZ);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		
		session.removeAttribute("isvykePazyma");
		return mapper.findForward(Constants.CONTINUE);
	}

}
