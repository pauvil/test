package com.algoritmusistemos.gvdis.web.actions.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lt.solver.gvdis.dal.JdbcUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;



public class LogoutAction extends Action 
{
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) 
	{
		try{
			JdbcUtils.closeSessionConnnection(request);
			HttpSession session = request.getSession();
			session.invalidate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
    	return (mapping.findForward(Constants.CONTINUE));
	}
}