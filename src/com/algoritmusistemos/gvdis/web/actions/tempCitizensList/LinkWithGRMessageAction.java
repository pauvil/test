package com.algoritmusistemos.gvdis.web.actions.tempCitizensList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;

public class LinkWithGRMessageAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		request.getSession().setAttribute(Constants.CENTER_STATE, Constants.LINK_WITH_GR_PERFORM);		
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_ASSIGN_GR);
	    return (mapping.findForward(Constants.CONTINUE));
	}
}