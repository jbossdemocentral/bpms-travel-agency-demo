package acme.service.soap.hotelws.exception;




public class DataBaseException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public DataBaseException(Throwable p_Cause)
	{
		super(p_Cause);
	}
	
	public String getMessage() 
	{
		StringBuffer v_Message = new StringBuffer("There is a problem with the database connection. Please contact the server administrator.");
		return v_Message.toString();
	}
}
