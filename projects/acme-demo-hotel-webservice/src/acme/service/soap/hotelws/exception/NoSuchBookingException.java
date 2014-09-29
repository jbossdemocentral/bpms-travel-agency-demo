package acme.service.soap.hotelws.exception;




public class NoSuchBookingException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	private Integer m_BookingId;
	
	public NoSuchBookingException(Integer p_BookingId)
	{
		super();
		m_BookingId = p_BookingId;
	}
	
	public String getMessage() 
	{
		StringBuffer v_Message = new StringBuffer("There is no such booking with the code ");
		v_Message.append(m_BookingId);
		v_Message.append(". Please select a valid booking.");
		
		return v_Message.toString();
	}
}
