package com.algoritmusistemos.gvdis.web.actions.logging;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.delegators.UtilDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.exceptions.ObjectNotFoundException;
import com.algoritmusistemos.gvdis.web.forms.LoginForm;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;




public class LoginAction extends Action 
{
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		throws DatabaseException, ObjectNotFoundException,Exception
	{
		HttpSession session = request.getSession();
		LoginForm lf = (LoginForm)form;
		
		Object user = session.getAttribute("userLogin");

		if (user != null && ! ((String) user).toUpperCase().equals(lf.getLogin().toUpperCase()) ){
			HibernateUtils.closeSession();
			HibernateUtils.sessionFactory = null;
			System.out.println("nesutampa useriai");
		}
		
		session.setAttribute("userLogin", lf.getLogin());
		session.setAttribute("userPassword", lf.getPassword());
		
		try{
		String[] istaiga = UserDelegator.getInstance().getDarbIstaiga(request);

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	    formatter.setLenient(false);
		Date created = new Date(session.getCreationTime());
		
		System.out.println("CreationTime: "+ formatter.format(created));

		System.out.println("istaiga: "+istaiga[0]+ " - " +istaiga[1]);
		session.setAttribute("userIstaigaId", Long.valueOf(istaiga[0]));
		session.setAttribute("userIstaiga", istaiga[1]);
		
		String[] darbuotojas = UserDelegator.getInstance().getDarbuotojas(request);
		
		System.out.println("userLogin: "+lf.getLogin());
		
		System.out.println("darbuotojas V: "+darbuotojas[0]);
		System.out.println("darbuotojas P: "+darbuotojas[1]);
		            
		session.setAttribute("userName", darbuotojas[0]);
		session.setAttribute("userSurname", darbuotojas[1]);

		int userStatus = UserDelegator.getInstance().getUserStatus(request);
		session.setAttribute("userStatus", new Integer(userStatus));
		System.out.println("statusas: "+userStatus);
		
		Set roles = UserDelegator.getInstance().getUserRoles(request, lf.getLogin());
		System.out.println("roles: "+roles);
		session.setAttribute("userRoles", roles);

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		List valstybes = UtilDelegator.getInstance().getValstybes(request);
		session.setAttribute("salys", valstybes);
		List valstybesbelietuvos = UtilDelegator.getInstance().getValstybesBeLietuvos(request);
		session.setAttribute("salysbelietuvos", valstybesbelietuvos);		
		List pilietybes = UtilDelegator.getInstance().getPilietybes(request);
		session.setAttribute("pilietybes", pilietybes);
		List pilietybesbenull = UtilDelegator.getInstance().getPilietybesBeNull(request);
		session.setAttribute("pilietybesbenull", pilietybesbenull);
		
		
       if("0".equals(istaiga[0]) || "null".equals(darbuotojas[0]) || "null".equals(darbuotojas[1]) || -1 == userStatus)
		{
			session.setAttribute("userNotGood", "");
			session.removeAttribute("userLogin");
			try{response.sendRedirect("login.do");}catch(IOException ioe){}
			return null;
		}

		}
		catch(DatabaseException dbe)
		{
			dbe.printStackTrace();
			throw dbe;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}		

		return mapping.findForward(Constants.CONTINUE);
	}
}