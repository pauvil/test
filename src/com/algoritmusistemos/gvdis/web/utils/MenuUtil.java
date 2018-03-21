package com.algoritmusistemos.gvdis.web.utils;

import javax.servlet.http.HttpServletRequest;


public class MenuUtil 
{
	public static String appropriateClass(String menu_cell,HttpServletRequest request)
	{
		if(menu_cell.equals(String.valueOf(request.getSession().getAttribute("menu_cell"))))return "menu_active";
		else return "menu2";
	}
	public static String headerAppropriateClass(String menu_cell,HttpServletRequest request)
	{
		if(menu_cell.equals(String.valueOf(request.getSession().getAttribute("menu_cell"))))return "menu_active";
		else return "menu1";
	}	
}
