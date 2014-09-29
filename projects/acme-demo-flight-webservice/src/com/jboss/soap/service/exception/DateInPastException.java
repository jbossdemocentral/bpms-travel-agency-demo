package com.jboss.soap.service.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateInPastException extends Exception
{	
	private static final long serialVersionUID = 1L;
	
	private Date m_BookingDate;

	public DateInPastException(Date p_BookingDate)
	{
		super();
		m_BookingDate = p_BookingDate;
	}
	
	
	public String getMessage() 
	{
		SimpleDateFormat v_Format = new SimpleDateFormat("dd-MMM-yy");
		
		StringBuffer v_Message = new StringBuffer("The booking date ");
		v_Message.append(v_Format.format(m_BookingDate));
		v_Message.append(" is invalid. The booking date cannot be in the past.");
		
		return v_Message.toString();
	}

}
