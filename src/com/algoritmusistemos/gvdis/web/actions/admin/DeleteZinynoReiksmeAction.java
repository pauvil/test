package com.algoritmusistemos.gvdis.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.ZinynaiDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Zinynas;

public class DeleteZinynoReiksmeAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
    	Long actZinynasId = (Long)session.getAttribute("actZinynasId");
    	Zinynas actZinynas = ZinynaiDelegator.getInstance().getZinynas(request, actZinynasId.longValue());
    	request.setAttribute("actZinynas", actZinynas);
    	
    	long id = Long.parseLong(request.getParameter("id"));
    	try {
    		ZinynaiDelegator.getInstance().deleteZinynoReiksme(request, id);
    	}
    	catch (DatabaseException de){
    	    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_LIST_ZINYNO_REIKSMES);
    	    request.setAttribute("deleteError", "on");
    	    return mapping.findForward("failure");
    	}
	    return mapping.findForward(Constants.CONTINUE);
	}
}
