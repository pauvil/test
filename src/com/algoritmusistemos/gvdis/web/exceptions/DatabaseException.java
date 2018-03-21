package com.algoritmusistemos.gvdis.web.exceptions;

public class DatabaseException extends Exception
{
	public DatabaseException(String error)
	{
		super(error);
	}
	
	public DatabaseException(Exception parent)
	{
		super(parent);
	}
	
	public DatabaseException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
