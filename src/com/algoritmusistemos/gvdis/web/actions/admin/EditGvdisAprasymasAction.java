package com.algoritmusistemos.gvdis.web.actions.admin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.AprasymaiDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.forms.EditGvdisAprasymasForm;
import com.algoritmusistemos.gvdis.web.persistence.AprModulis;

public class EditGvdisAprasymasAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws DatabaseException, PermissionDeniedException {

		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
		EditGvdisAprasymasForm frm = (EditGvdisAprasymasForm)form;
		long id = Long.parseLong(request.getParameter("modulioId"));

		AprModulis mod = AprasymaiDelegator.getInstance().getAprModulis(request, id);

		Calendar cal = Calendar.getInstance();
		
		mod.setAprasymas(frm.getAprasymas());
		mod.setLogikosAprasymas(frm.getLogikosAprasymas());
		mod.setKoregavimoData(cal.getTime());
		
		AprasymaiDelegator.getInstance().saveAprasymas(request, mod);
		
		return mapping.findForward(Constants.CONTINUE);
	}
}
