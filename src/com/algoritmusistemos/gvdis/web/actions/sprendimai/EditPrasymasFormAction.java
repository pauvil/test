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
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.PrasymasKeistiDuomenis;

public class EditPrasymasFormAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InternalException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.SPR_EDIT_PRASYMAS_FORM);
	    request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_REGISTER_REQUEST_DATA_CHANGE);	    
		UserDelegator.checkPermission(request, new String[]{"RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK"});
	    
	    try {
	    	Long id = Long.valueOf((String)request.getParameter("id"));
	    	session.setAttribute("actPrasymasId", id);
			PrasymasKeistiDuomenis pras = SprendimaiDelegator.getInstance().getPrasymas(request, id.longValue());
			pras.getRegNr(); // tikrina ar dar yra toks, gal jau istrintas
			if(pras.getTipas()==2){
				request.setAttribute("addressString", pras.getNaikinamasAdresas());
			}
	    }
	    catch (Exception e){
	    	throw new InternalException("Blogi praðymo parametrai", e);
	    }
    	
    	return (mapping.findForward(Constants.CONTINUE));
	}
}
