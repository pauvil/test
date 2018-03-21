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
import com.algoritmusistemos.gvdis.web.forms.ZinynoReiksmeForm;
import com.algoritmusistemos.gvdis.web.persistence.Zinynas;
import com.algoritmusistemos.gvdis.web.persistence.ZinynoReiksme;

public class CreateZinynoReiksmeAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, PermissionDeniedException
	{
		HttpSession session = request.getSession();
		ZinynoReiksmeForm zForm = (ZinynoReiksmeForm)form;
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
    	Long actZinynasId = (Long)session.getAttribute("actZinynasId");
    	Zinynas actZinynas = ZinynaiDelegator.getInstance().getZinynas(request, actZinynasId.longValue());

    	ZinynoReiksme zr = new ZinynoReiksme();
		zr.setKodas(zForm.getKodas());
		zr.setZinynas(actZinynas);
		zr.setPavadinimas(zForm.getPavadinimas());
		zr.setKomentaras(zForm.getKomentaras());
		ZinynaiDelegator.getInstance().saveZinynoReiksme(request, zr);
		
	    return mapping.findForward(Constants.CONTINUE);
	}
}
