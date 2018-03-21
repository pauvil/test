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

public class GvnaPazymaFormAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
        request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_GVNA_PAZ);
	    session.setAttribute(Constants.CENTER_STATE, Constants.PAZ_GVNA_FORM);
        session.setAttribute("menu_cell","gvnapazymaform");
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
	    
    	session.removeAttribute("gvnaVaikai");
    	session.removeAttribute("gvnaPazyma");
    	session.removeAttribute("gvnaGvtDataNuo");
		session.removeAttribute("gvnaSavivaldybe");
		session.removeAttribute("gvnaVaikoGvt");
		session.removeAttribute("prasymoDataYra");
    	return mapping.findForward(Constants.CONTINUE);
	}
}
