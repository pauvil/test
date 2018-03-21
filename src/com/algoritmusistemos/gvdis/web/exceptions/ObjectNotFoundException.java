package com.algoritmusistemos.gvdis.web.exceptions;

public class ObjectNotFoundException extends Exception
{
	public ObjectNotFoundException(String userMessage)
	{ 
		super(userMessage);
	}
	
	public ObjectNotFoundException(Throwable cause)
	{
		super(cause);
	}
	
	public ObjectNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
