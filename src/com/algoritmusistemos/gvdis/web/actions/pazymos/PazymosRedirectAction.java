package com.algoritmusistemos.gvdis.web.actions.pazymos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.algoritmusistemos.gvdis.web.delegators.PazymosDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.InternalException;
import com.algoritmusistemos.gvdis.web.exceptions.PermissionDeniedException;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.GvPazyma;

public class PazymosRedirectAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws DatabaseException, InternalException, PermissionDeniedException {
		HttpSession session = request.getSession();		
		UserDelegator.checkPermission(request, new String[] {"RL_GVDIS_GL_SKAIT", "RL_GVDIS_SS_SKAIT", "RL_GVDIS_GL_TVARK", "RL_GVDIS_SS_TVARK" });
		long id = Long.parseLong(request.getParameter("id"));
		
		try {
			GvPazyma pazyma = PazymosDelegator.getInstance().getGvPazyma(request, id);
			Asmuo asmuo = pazyma.getGyvenamojiVieta() != null ? pazyma.getGyvenamojiVieta()
					.getAsmuo() : pazyma.getDeklaracija().getAsmuo();

			session.setAttribute("gvPazyma", pazyma);
			
			if (pazyma.getGyvenamojiVieta() != null) {
				if (!"V".equals(pazyma.getGyvenamojiVieta().getGvtTipas())) {
					if (asmuo != null && asmuo.getAsmMirtiesData() != null && asmuo.getAsmMirtiesData().getTime() <= pazyma.getPazymosData().getTime())
						response.sendRedirect("mirepazyma.do?id=" + id);
					else
						response.sendRedirect("gvpazyma.do?id=" + id);
				}
				if ("V".equals(pazyma.getGyvenamojiVieta().getGvtTipas())) {
					response.sendRedirect("isvykepazyma.do?id=" + id);
				}
			} else {					
				if(pazyma.getDeklaracija().getLaikinasAsmuo() != null) //Asmeniui kuris neturi asmens kodo
					response.sendRedirect("gvpazyma.do?id=" + id);				
			}
			
			
		} catch (Exception e) {
			throw new InternalException(e);
		}
		return null;
	}

}
