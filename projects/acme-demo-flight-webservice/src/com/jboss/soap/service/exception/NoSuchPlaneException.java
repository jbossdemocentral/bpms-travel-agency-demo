package com.jboss.soap.service.exception;




public class NoSuchPlaneException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	private Integer m_PlaneId;
	
	public NoSuchPlaneException(Integer p_PlaneId)
	{
		super();
		m_PlaneId = p_PlaneId;
	}
	
	public String getMessage() 
	{
		StringBuffer v_Message = new StringBuffer("There is no such plane with the code ");
		v_Message.append(m_PlaneId);
		v_Message.append(". Please select a valid plane.");
		
		return v_Message.toString();
	}
}
