package com.algoritmusistemos.gvdis.web.actions.deklaracijos;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.DeklaracijosDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.persistence.Deklaracija;

public class DeleteDeclarationBaigtaAction extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ObjectNotFoundException,DatabaseException, Exception
	{
		Long l = new Long(request.getParameter("id"));
		Deklaracija d = DeklaracijosDelegator.getInstance(request).getDeklaracija(l, request);
		
		Constants.Println(request, "AAAAAAAAAAAAAAAAA_____DEKL___ID " + d.getId());
		if (d == null){
			Constants.Println(request, "AAAAAAAAAAAAAAAAAA____NERA___DEKLARACIJOS");
		}
		
		HttpSession session = request.getSession();		
		String journalType = request.getParameter("journalType");
		
		int i = d.getCalcTipas();
		Constants.Println(request, "AAAAAAAAAAAAAAAAAA____DEKLARACIJOS___TIPAS" + i);
		if (i != Deklaracija.TYPE_UNFINISHED && i != Deklaracija.ELECTRONIC_NOTVALID) {
			if (-1 == DeklaracijosDelegator.getInstance(request).deleteDeclarationBaigta(request, d, journalType)) {
				session.setAttribute(Constants.CENTER_STATE, Constants.CANNOT_DELETE_DECLARATION);
				return (mapping.findForward(Constants.CANNOT_DELETE_DECLARATION));
			}
		} else if (-1 == DeklaracijosDelegator.getInstance(request).deleteDeclaration(request, d)) {
			session.setAttribute(Constants.CENTER_STATE, Constants.CANNOT_DELETE_DECLARATION);
			return (mapping.findForward(Constants.CANNOT_DELETE_DECLARATION));
		}				
				
		try {
			if ("0".equals(journalType))
				response.sendRedirect("journal.do?journalType=0");
			if ("1".equals(journalType))
				response.sendRedirect("journal.do?journalType=1");
			if ("4".equals(journalType))
				response.sendRedirect("journal.do?journalType=4");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
