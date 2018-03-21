package com.algoritmusistemos.gvdis.web.actions.admin;

import java.util.Set;

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
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Zinynas;

public class ListZinynoReiksmesAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InternalException, ObjectNotFoundException, PermissionDeniedException
	{
	    HttpSession session = request.getSession();
	    session.setAttribute(Constants.CENTER_STATE, Constants.ADMIN_LIST_ZINYNO_REIKSMES);
		UserDelegator.checkPermission(request, "RL_GVDIS_ADMIN");

	    Zinynas zinynas = null;
	    if (request.getParameter("id") != null) try {
	    	long id = Long.parseLong(request.getParameter("id"));
	    	zinynas = ZinynaiDelegator.getInstance().getZinynas(request, id);
	    	session.setAttribute("actZinynasId", new Long(zinynas.getId()));
	    	request.setAttribute("actZinynas", zinynas);
	    }
	    catch (NumberFormatException nfe){
	    	throw new InternalException("Blogas þinyno ID", nfe);
	    }
	    else {
	    	Long actZinynasId = (Long)session.getAttribute("actZinynasId");
	    	zinynas = ZinynaiDelegator.getInstance().getZinynas(request, actZinynasId.longValue());
	    	request.setAttribute("actZinynas", zinynas);
	    }
	    
	    if (zinynas != null){
	    	Set zinyno_reiksmes = ZinynaiDelegator.getInstance().getZinynoReiksmes(request, zinynas.getKodas());
	    	request.setAttribute("reiksmes", zinyno_reiksmes);
	    }
	    
	    return (mapping.findForward(Constants.CONTINUE));
	}
}
