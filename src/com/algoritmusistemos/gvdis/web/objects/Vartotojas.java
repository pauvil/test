package com.algoritmusistemos.gvdis.web.objects;

import java.util.ArrayList;
import java.util.Iterator;

public class Vartotojas 
{
	private String login;
	private String password;
	private ArrayList roles = new ArrayList();
	
	public Vartotojas(String login,String password)
	{
		this.login = login;
		this.password = password;
	}
	
	public void setLogin(String val){login = val;}
	public String getLogin(){return login;}
	
	public void setPassword(String val){password = val;}
	public String getPassword(){return password;}
	
	public void setRoles(ArrayList r){roles = r;}
	public boolean isUserHaveRole(String role)
	{
		Iterator it = roles.iterator();
		while(it.hasNext())
		{
			String listRole = (String)it.next();
			if(listRole.equals(role)) return true;
		}
		return false;		
	}
}
