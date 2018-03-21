package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;

public class GvnaDeklaracijaAsmKodasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws PermissionDeniedException, 
	       DatabaseException
	{
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});	
		HttpSession session = request.getSession();
		session.removeAttribute("prohibited");
        session.setAttribute("menu_cell","GvnaDeklaracijaAsmKodas");
	    session.setAttribute(Constants.CENTER_STATE, Constants.GVNA_DECLARATION_CODE_FORM);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);
	    session.setAttribute("declaration_mode",Constants.GVNA_DECLARATION_FORM);
	    session.removeAttribute("idForEdit");
    	//session.setAttribute("savivaldybes",AdresaiDelegator.getInstance().getRajonoSavivaldybes(request)); //kom ju.k 2007.08.24 
	    return (mapping.findForward(Constants.CONTINUE));
	}
}