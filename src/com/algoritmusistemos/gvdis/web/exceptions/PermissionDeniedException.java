package com.algoritmusistemos.gvdis.web.exceptions;

public class PermissionDeniedException extends Exception
{
	public PermissionDeniedException(String permission, String userName)
	{
		super("Vartotojas " + userName + " neturi reikalingos roles " + permission);
	}
}
