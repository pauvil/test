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

public class IsvDeklaracijaAsmKodasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws PermissionDeniedException
	{
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK", "RL_GVDIS_UZ_REIK_MINIST_TVARK"});
	    HttpSession session = request.getSession();
	    session.removeAttribute("prohibited");
        session.setAttribute("menu_cell","IsvDeklaracijaAsmKodas");
        
        if (request.getParameter("outType") != null && request.getParameter("outType").equals("1")){ 
    		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_UZ_REIK_MINIST_TVARK"});
        	//Gyvenamosios vietos keitimas uþsienyje
		    session.setAttribute(Constants.CENTER_STATE, Constants.CHNG_OUT_DECLARATION_CODE_FORM);
		    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);	    
		    session.setAttribute("declaration_mode",Constants.CHNG_OUT_DECLARATION_FORM);
        	
        } else {
		    session.setAttribute(Constants.CENTER_STATE, Constants.OUT_DECLARATION_CODE_FORM);
		    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECLARATION);	    
		    session.setAttribute("declaration_mode",Constants.OUT_DECLARATION_FORM);
        }
	    
	    session.removeAttribute("idForEdit");	    
		String dekl = "T";
		session.setAttribute("isvdekl", dekl);
	    return (mapping.findForward(Constants.CONTINUE));
	}
}