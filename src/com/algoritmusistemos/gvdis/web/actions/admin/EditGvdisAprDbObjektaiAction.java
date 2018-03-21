package com.algoritmusistemos.gvdis.web.actions.admin;

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
import com.algoritmusistemos.gvdis.web.forms.EditGvdisAprDbObjektaiForm;
import com.algoritmusistemos.gvdis.web.persistence.AprDbObjektas;

public class EditGvdisAprDbObjektaiAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws DatabaseException, PermissionDeniedException {
		
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");
		
		EditGvdisAprDbObjektaiForm frm = (EditGvdisAprDbObjektaiForm)form;
		long id = Long.parseLong(request.getParameter("id"));

		if(id!=0){
			AprDbObjektas ob = AprasymaiDelegator.getInstance().getAprDbObjektas(request, id);
			ob.setObjektas(frm.getObjektas());
			ob.setParametrai(frm.getParametrai());
			ob.setTipas(frm.getTipas());
			ob.setSchema(frm.getSchema());
			ob.setKomentarai(frm.getKomentarai());
			
			AprasymaiDelegator.getInstance().saveAprDbObjektas(request, ob);
		}
		else{
			AprasymaiDelegator.getInstance().addDbObjectDescription(request, Long.parseLong(frm.getModulioId()), frm.getObjektas(), frm.getParametrai(), frm.getTipas(), frm.getSchema(), frm.getKomentarai());
		}
			
		return mapping.findForward(Constants.CONTINUE);

	}

}
