package com.jboss.soap.service.exception;

import java.text.SimpleDateFormat;

import com.jboss.soap.service.Flight;

public class FlightNotAvailableException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	private Flight desiredFlight;
	
	public FlightNotAvailableException(Flight availFlight)
	{
		super();
		desiredFlight = availFlight;
	}
	
	public String getMessage() 
	{
		SimpleDateFormat v_Format = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat v_HourFormat = new SimpleDateFormat("kk:mm");
		
		StringBuffer v_Message = new StringBuffer("The flight from ");
		v_Message.append(desiredFlight.getCompany());
		v_Message.append(" leaving ");
		v_Message.append(desiredFlight.getStartCity());
		v_Message.append(" at ");
		v_Message.append(v_HourFormat.format(desiredFlight.getStartTime()));
		v_Message.append(" reaching ");
		v_Message.append(desiredFlight.getTargetCity());
		v_Message.append(" at ");
		v_Message.append(v_HourFormat.format(desiredFlight.getEndTime()));
		v_Message.append(" is not available on ");
		v_Message.append(v_Format.format(desiredFlight.getTravelDate()));
		v_Message.append(". Please select a valid booking.");
		
		return v_Message.toString();
	}
}
