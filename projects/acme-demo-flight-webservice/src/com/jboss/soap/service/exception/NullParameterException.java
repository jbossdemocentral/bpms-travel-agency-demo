package com.jboss.soap.service.exception;




public class NullParameterException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	private String m_ParameterName;
	
	public NullParameterException(String p_ParameterName)
	{
		super();
		m_ParameterName = p_ParameterName;
	}
	
	public String getMessage() 
	{
		StringBuffer v_Message = new StringBuffer("The parameter ");
		v_Message.append(m_ParameterName);
		v_Message.append(" is null. Please fill the parameter before recalling the service.");
		
		return v_Message.toString();
	}
}
