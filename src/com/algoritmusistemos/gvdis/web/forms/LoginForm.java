package com.algoritmusistemos.gvdis.web.forms;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;

public class LoginForm extends ActionForm
{
	private String login;
	private String password;	
	/////////////////////////////////////
	public void setLogin(String login){this.login = login;}
	public String getLogin(){return this.login;}	
	public void setPassword(String password){this.password = password;}
	public String getPassword(){return this.password;}	

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		try {
			UserDelegator.getInstance().checkLoginParameters(login, password);
			
			if (!UserDelegator.getInstance().chkVersija(login, password)) {
				errors.add("login", new ActionMessage("error.login"));
				request.setAttribute("loginVersionError", "Bloga versija");
			}	
		
		}
		catch (DatabaseException e){
			errors.add("login", new ActionMessage("error.login"));
			request.setAttribute("loginError", e.getMessage());
		}
		return errors;
	}
}
