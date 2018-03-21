package com.algoritmusistemos.gvdis.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AprasymaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.JournalDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.AprDbObjektas;
import com.algoritmusistemos.gvdis.web.persistence.Istaiga;


public class EditGvdisAprDbObjektaiFormAction extends Action{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws InternalException, PermissionDeniedException
		{
		    HttpSession session = request.getSession();
		    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_GVDIS_APRASYMAS_EDIT_DB);
			UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
			
			AprDbObjektas db;
		    try {
		    	long id = Long.parseLong(request.getParameter("id"));
		    	long modId = Long.parseLong(request.getParameter("modulioId"));
		    	if(id!=0){
			    	db = AprasymaiDelegator.getInstance().getAprDbObjektas(request, id);		    		
			    	request.setAttribute("dbobjektas", db);
		    	}
		    	else{
		    		request.removeAttribute("dbobjektas");
		    	}
		    }
		    catch (Exception e){
		    	throw new InternalException("Blogas ID", e);
		    }

		    return (mapping.findForward(Constants.CONTINUE));
		}
	
}
