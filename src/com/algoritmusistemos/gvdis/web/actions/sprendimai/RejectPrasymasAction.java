package com.algoritmusistemos.gvdis.web.actions.sprendimai;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class RejectPrasymasAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, PermissionDeniedException
	{
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
    	long id = Long.parseLong(request.getParameter("id"));
    	request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_REQUEST_DATA_CHANGE);
    	PrasymasKeistiDuomenis prasymas = SprendimaiDelegator.getInstance().getPrasymas(request, id);
    	prasymas.setBusena(2);
    	SprendimaiDelegator.getInstance().savePrasymas(request, prasymas);
    	
	    return mapping.findForward(Constants.CONTINUE);
	}
}
