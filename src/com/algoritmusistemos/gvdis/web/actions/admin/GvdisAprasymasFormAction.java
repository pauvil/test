package com.algoritmusistemos.gvdis.web.actions.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AprasymaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.GvdisAprasymasForm;

public class GvdisAprasymasFormAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws InternalException, PermissionDeniedException
		{
		    HttpSession session = request.getSession();
	        session.setAttribute("menu_cell","gvdisaprasymasform");
		    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_GVDIS_APRASYMAS);
			UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
			
			request.removeAttribute("meniuGrupes");
			request.removeAttribute("meniuPunktai");
			
			session.removeAttribute("meniuGrupesKodas");
			session.removeAttribute("meniuPunktoKodas");
			
			request.setAttribute("meniuGrupes", AprasymaiDelegator.getInstance().getMeniuGrupesList(request));
		
			session.removeAttribute("aprModulis");
			session.removeAttribute("dbObjektai");
			
		    return (mapping.findForward(Constants.CONTINUE));
		}
}
