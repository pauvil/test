package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;

public class CreateSprendimasFormAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
        session.setAttribute("menu_cell","createsprendimasform");	    
	    session.setAttribute(Constants.CENTER_STATE, Constants.SPR_CREATE_SPRENDIMAS_FORM);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_DECISION_DATA_CHANGE);	    
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
	    
//    	session.removeAttribute("actPrasymasId");
    	session.removeAttribute("actSprendimasId");


    	
    	session.setAttribute("createSprendimas", "T");
    	
    	List valstybes = UtilDelegator.getInstance().getValstybes(request);
    	session.setAttribute("valstybes", valstybes);
    	
    	if (request.getParameter("prasymasId") != null){
    		session.setAttribute("actPrasymasId", new Long(request.getParameter("prasymasId")));
    	}
    	
    	return mapping.findForward(Constants.CONTINUE);
	}
}
