package com.infy.eng.messaging.core;

public class MessageException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private String code;
	private int status;
	private String errorMessage;

	public MessageException(String code, String errorMessage, int status)
	{
		this.code = code;
		this.errorMessage = errorMessage;
		this.status = status;
	}

	public String getCode()
	{
		return code;
	}

	public int getStatus()
	{
		return status;
	}
	
	public String getErrorMessage() 
	{
		return errorMessage;
	}
}