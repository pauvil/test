package com.algoritmusistemos.gvdis.web.exceptions;

public class InternalException extends Exception
{
	public InternalException(String userMessage)
	{ 
		super(userMessage);
	}
	
	public InternalException(Throwable cause)
	{
		super(cause);
	}
	
	public InternalException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
