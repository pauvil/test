package com.algoritmusistemos.gvdis.web.filters;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.GenericJDBCException;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;


public class InitValuesFilter implements Filter 
{
	String realPath;
	
    public void init(FilterConfig arg0) 
    	throws ServletException 
    {
    	realPath = arg0.getServletContext().getRealPath("/");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
        throws IOException, ServletException, GenericJDBCException
    {
    	HttpServletRequest request = (HttpServletRequest)req;
    	HttpSession session = request.getSession();
    	String dateString = (new SimpleDateFormat("yyyy MMMM dd")).format(new Date()) + " d.";
    	request.setAttribute("dateString", dateString);
    	request.setAttribute("realPath", realPath);
    	request.setAttribute("currentURL", request.getRequestURL());
    	Locale.setDefault(new Locale("lt", "LT"));
    	
    	request.setCharacterEncoding("Windows-1257");
		String path = request.getServletPath();

		request.setAttribute(Constants.HELP_CODE,Constants.HLP_GVDIS_DEFAULT);
		
		String ws_useris = java.lang.System.getProperty("username");
		
    	try {
    		if (path.equalsIgnoreCase("/loginform.do") || path.equalsIgnoreCase("/login.do")){
    			chain.doFilter(req, resp);
    		}
    		else if (session.getAttribute("userLogin") == null || session.getAttribute("userPassword") == null){
    			((HttpServletResponse)resp).sendRedirect("loginform.do");
    		}
    		else if (ws_useris != null && session.getAttribute("userLogin").equals(ws_useris) && !path.equalsIgnoreCase("/logout.do")) {
    			//Jeigu matosi, kad useris atejo su WS loginu- neleidziame dirbti!
    			((HttpServletResponse)resp).sendRedirect("logout.do"); 
    			//chain.doFilter(req, resp);
    		}
    		else if (!"/changepasswordform.do".equals(path) && !"/changepassword.do".equals(path) && !"/changepassworddone.do".equals(path) && !"/logout.do".equals(path) && UserDelegator.getInstance().hasToChangePassword(request)){
    			((HttpServletResponse)resp).sendRedirect("changepasswordform.do?forced=1");
    		}
    		else if (!"/logout.do".equals(path) && !UserDelegator.getInstance().chkVersija(request)) {
    			((HttpServletResponse)resp).sendRedirect("logout.do");
    		}
    		else {
    			UserDelegator.getInstance().getUserRoles(request, (String)session.getAttribute("userLogin"));
    			chain.doFilter(req, resp);
    		}
    	}
    	catch (DatabaseException ex){
    		ex.printStackTrace();
    		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
    		ex.printStackTrace(pw);
    		
    		
    	}
    	finally {
    		HibernateUtils.closeSession();
    	}
    }


    public void destroy() 
    {
    }
}
