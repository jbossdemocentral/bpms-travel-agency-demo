package acme.service.soap.hotelws.exception;

import java.text.SimpleDateFormat;

import acme.service.soap.hotelws.Resort;


public class HotelNotAvailableException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	private Resort desiredResort;
	
	public HotelNotAvailableException(Resort availResort)
	{
		super();
		desiredResort = availResort;
	}
	
	public String getMessage() 
	{
		SimpleDateFormat v_Format = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat v_HourFormat = new SimpleDateFormat("kk:mm");
		
		StringBuffer v_Message = new StringBuffer("The hotel ");
		v_Message.append(desiredResort.getHotelName());
		v_Message.append(" in ");
		v_Message.append(desiredResort.getHotelCity());
		v_Message.append(" is not available from ");
		v_Message.append(v_HourFormat.format(desiredResort.getTargetDate()));
		v_Message.append(". Please select another booking.");
		
		return v_Message.toString();
	}
}
