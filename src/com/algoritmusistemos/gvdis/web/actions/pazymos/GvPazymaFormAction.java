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

public class GvPazymaFormAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
        session.setAttribute("menu_cell","gvpazymaform");	
	    session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_GV_FORM);
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_GV_PAZ);
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
	    session.removeAttribute("gvVaikai");
    	session.removeAttribute("gvVaikoGvt");
    	session.removeAttribute("gvDekData");
    	session.removeAttribute("prasymoDataYra");
    	session.removeAttribute("gvPazyma");
    	
    	return mapping.findForward(Constants.CONTINUE);
	}
}
