package com.algoritmusistemos.gvdis.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.algoritmusistemos.gvdis.web.persistence.ZinynoReiksme;

public class EditZinynoReiksmeAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws DatabaseException, PermissionDeniedException
	{
		ZinynoReiksmeForm zForm = (ZinynoReiksmeForm)form;
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
		long id = Long.parseLong(zForm.getId());
		ZinynoReiksme zr = ZinynaiDelegator.getInstance().getZinynoReiksme(request, id);
		zr.setKodas(zForm.getKodas());
		zr.setPavadinimas(zForm.getPavadinimas());
		zr.setKomentaras(zForm.getKomentaras());
		ZinynaiDelegator.getInstance().saveZinynoReiksme(request, zr);
		
	    return mapping.findForward(Constants.CONTINUE);
	}
}
