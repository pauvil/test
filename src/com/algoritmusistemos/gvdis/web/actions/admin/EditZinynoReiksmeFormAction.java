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
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Zinynas;

public class EditZinynoReiksmeFormAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InternalException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_EDIT_ZINYNO_REIKSME_FORM);
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
	    
	    try {
	    	Long actZinynasId = (Long)session.getAttribute("actZinynasId");
	    	Zinynas actZinynas = ZinynaiDelegator.getInstance().getZinynas(request, actZinynasId.longValue());
	    	request.setAttribute("actZinynas", actZinynas);
	    	
	    	Long actZinynoReiksmeId = Long.valueOf(request.getParameter("id"));
	    	session.setAttribute("actZinynoReiksmeId", actZinynoReiksmeId);
	    }
	    catch (Exception e){
	    	throw new InternalException("Blogas þinyno arba þinyno reikðmës ID", e);
	    }
	    
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
