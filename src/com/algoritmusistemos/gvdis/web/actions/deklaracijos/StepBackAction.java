package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;

public class StepBackAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
	    HttpSession session = request.getSession();
	    ArrayList alist = (ArrayList)session.getAttribute("address");
	    int step = Integer.parseInt(request.getParameter("step"));
	    removeAllFrom(alist,step,session);
	    session.setAttribute("address",alist);
	    try{
	    	response.sendRedirect("SelectPlace.do?type="+(step-1)+"&id="+session.getAttribute("prev"));
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return (mapping.findForward(Constants.CONTINUE));
	}
	public void removeAllFrom(ArrayList alist,int from,HttpSession session)
	{
		while(alist.size() >= from - 1)alist.remove(alist.size()-1);
	}	
}
