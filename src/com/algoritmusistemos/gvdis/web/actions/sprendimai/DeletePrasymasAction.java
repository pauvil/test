package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.SprendimaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;

public class DeletePrasymasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_REQUEST_DATA_CHANGE);
    	long id = Long.parseLong(request.getParameter("id"));
    	PrasymasKeistiDuomenis pkd = SprendimaiDelegator.getInstance().getPrasymas(request, id);
    	if(pkd != null && pkd.getBusena()==1){
    		session.setAttribute(Constants.CENTER_STATE, Constants.SPR_LIST_PRASYMAI);
      	    request.setAttribute("deleteError", "on");
      	    return mapping.findForward("failure");
    	}
    	try {
    		SprendimaiDelegator.getInstance().deletePrasymas(request, id);
    	}
    	catch (DatabaseException de){
    	    session.setAttribute(Constants.CENTER_STATE, Constants.SPR_LIST_PRASYMAI);
    	    request.setAttribute("deleteError", "on");
    	    return mapping.findForward("failure");
    	}
	    return mapping.findForward(Constants.CONTINUE);
	}
}
