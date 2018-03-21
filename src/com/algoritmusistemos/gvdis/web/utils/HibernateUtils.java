package com.algoritmusistemos.gvdis.web.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.algoritmusistemos.gvdis.web.Constants;

public class HibernateUtils 
{
	private static final ThreadLocal session = new ThreadLocal();	
	private static final ThreadLocal connection = new ThreadLocal();
	public static SessionFactory sessionFactory;

	public static Session currentSession(HttpServletRequest request) 
	{
		// 1. Susikonfiguruojam session factory, jei reikia
		
		if (null == sessionFactory){
			try {
				Configuration cfg = new Configuration();
				cfg.configure("/hibernate.cfg.xml");
				sessionFactory = cfg.buildSessionFactory();
			} 
			catch (Exception ex){
				ex.printStackTrace();
				throw new ExceptionInInitializerError(ex);
			}
		}
		
		// 2. Atidarome ir graþiname sesija
		Session s = (Session) session.get();
		if (null == s) try {
			String login = (String)request.getSession().getAttribute("userLogin");
			String password = (String)request.getSession().getAttribute("userPassword");
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//Connection conn = DriverManager.getConnection(Constants.CONNECTION_STRING, login, password);
			Connection conn = DriverManager.getConnection(Constants.getDB(), login, password);
			//InitialContext ic = new InitialContext();
			//DataSource ds = (DataSource)ic.lookup("java:comp/env/jdbc/KmtsDB");
			//Connection conn = ds.getConnection(/*login, password*/);


			s = sessionFactory.openSession(conn);
			session.set(s);
			connection.set(conn);
		}
		catch (Exception ex){
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		return s;
	}



	public static void closeSession() 
	{
		Session s = (Session)session.get();
		Connection c = (Connection)connection.get();
		if (s != null){
			s.close();
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		session.set(null);
	}
}
