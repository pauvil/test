package com.algoritmusistemos.gvdis.web.actions.tempCitizensList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.LaikinasAsmuo;

public class TempCitizenAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ObjectNotFoundException,DatabaseException
	{
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CENTER_STATE, Constants.TEMP_CITIZEN);
        request.setAttribute(Constants.HELP_CODE, Constants.HLP_GVDIS_ASSIGN_GR);
		try{
			long id = Long.parseLong(request.getParameter("id"));
			LaikinasAsmuo laikinasAsmuo = UserDelegator.getInstance().getTempCitizen(id,request);
		    request.setAttribute("tempCitizen",laikinasAsmuo);
		    //request.setAttribute("tempCitizenDeclaration",DeklaracijosDelegator.getInstance(request).getDeklaracijaFromLaikinasAsmuo(laikinasAsmuo.getId(),request));
		}catch(NumberFormatException nfe)
		{
			throw new ObjectNotFoundException("LaikinasAsmuo with id["+request.getParameter("id")+"] not found");
		}
	    return (mapping.findForward(Constants.CONTINUE));
	}
}