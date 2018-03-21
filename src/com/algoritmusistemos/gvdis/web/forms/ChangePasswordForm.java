package com.algoritmusistemos.gvdis.web.forms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.algoritmusistemos.gvdis.web.delegators.UserDelegator;

public class ChangePasswordForm extends ActionForm
{
	static final int MIN_PASSWORD_LENGTH = 8;
	
	private String currPassword;	
	private String newPassword;	
	private String newPassword2;	

	public void setCurrPassword(String password){this.currPassword = password;}
	public String getCurrPassword(){return this.currPassword;}
	
	public void setNewPassword(String password){this.newPassword = password;}
	public String getNewPassword(){return this.newPassword;}
	
	public void setNewPassword2(String password){this.newPassword2 = password;}
	public String getNewPassword2(){return this.newPassword2;}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("userLogin");
		
		ActionErrors errors = new ActionErrors();
		if ("".equals(getNewPassword())){ 
			errors.add("newPassword", new ActionMessage("errors.emptyPassword"));
			request.setAttribute("newPasswordError", "Tuðèias slaptaþodis neleidþiamas");
		}
		else if (getNewPassword().length() < MIN_PASSWORD_LENGTH){
			errors.add("newPassword", new ActionMessage("errors.emptyPassword"));
			request.setAttribute("newPasswordError", "Naujas slaptaþodis turi bûti bent " + MIN_PASSWORD_LENGTH + " simboliø ilgio");
		}
		else if (currPassword.equals(getNewPassword())){
			errors.add("newPassword", new ActionMessage("errors.newPasswordEqualoldPassword"));
			request.setAttribute("newPasswordError", "Naujas slaptaþodis negali sutapti su dabartiniu slaptaþodiu");
		}
		else if (!getNewPassword().matches("((?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\\p{Punct}])).{1,}")){
			errors.add("newPassword", new ActionMessage("errors.newPasswordSymbols"));
			request.setAttribute("newPasswordError", "Slaptaþodyje turi bûti panaudoti bent po vienà visø trijø skirtingø tipø simboliø ið ðiø: raidþiø (a-z); skaièiø (0-9); specialiø simboliø (` ~ ! @ # $ % ^ & * ( ) _ + - = { } | [ ] \\ : \" ; ' < > ? , . /)");
		}
		else if (!newPassword2.equals(getNewPassword())){ 
			errors.add("newPassword2", new ActionMessage("errors.passwordsDontMatch"));
			request.setAttribute("newPasswordError", "Slaptaþodþiai nesutampa");
		}
		else try { 
			UserDelegator.getInstance().checkLoginParameters(username, getCurrPassword());
		}
		catch (Exception ex){
			errors.add("currPassword", new ActionMessage("errors.wrongCurrPassword"));
			request.setAttribute("newPasswordError", "Neteisingas dabartinis slaptaþodis");
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		newPassword = "";
		newPassword2 = "";
		currPassword = "";		
	}
}

