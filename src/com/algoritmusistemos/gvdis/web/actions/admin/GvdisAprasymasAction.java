package com.algoritmusistemos.gvdis.web.actions.admin;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

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
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.GvdisAprasymasForm;
import com.algoritmusistemos.gvdis.web.persistence.AprMeniuGrupe;
import com.algoritmusistemos.gvdis.web.persistence.AprModulis;


public class GvdisAprasymasAction extends Action {
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException, PermissionDeniedException
	{
	    HttpSession session = request.getSession(); 

	    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_GVDIS_APRASYMAS);
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
		GvdisAprasymasForm aprForm = (GvdisAprasymasForm)form;
		
		List mGr = AprasymaiDelegator.getInstance().getMeniuGrupesList(request);
		List mPunktas = AprasymaiDelegator.getInstance().getMeniuPunktaiList(request, aprForm.getMeniuGrupesKodas().longValue());
		
		request.setAttribute("meniuGrupes", mGr);
		request.setAttribute("meniuPunktai", mPunktas);
		
		
		if (aprForm.getMeniuPunktoKodas().longValue() != 0) {
			AprModulis aprModulis = AprasymaiDelegator.getInstance().getAprModulis(request, aprForm.getMeniuPunktoKodas().longValue());
			session.setAttribute("aprModulis", aprModulis);
			
			List dbObjektai = AprasymaiDelegator.getInstance().getDbObjektai(request, aprModulis.getModulioId().longValue());
			session.setAttribute("dbObjektai", dbObjektai);
			
			List aprVersijos = AprasymaiDelegator.getInstance().getAprasymoVersijosList(request, aprModulis.getModulioId().longValue());
			session.setAttribute("aprVersijos", aprVersijos);
			

		} else {
			session.removeAttribute("aprModulis");
			session.removeAttribute("dbObjektai");
			session.removeAttribute("aprVersijos");
		}
		
		return (mapping.findForward(Constants.CONTINUE));
	}

}

