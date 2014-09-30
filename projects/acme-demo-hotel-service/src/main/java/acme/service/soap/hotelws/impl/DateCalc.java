package acme.service.soap.hotelws.impl;

import java.util.Date;

public class DateCalc {

	   public int numberOfDays(String fromDate,String toDate)
	   {    
	       java.util.Calendar cal1 = new java.util.GregorianCalendar();
	       java.util.Calendar cal2 = new java.util.GregorianCalendar();

	       //split year, month and days from the date using StringBuffer.
	       StringBuffer sBuffer = new StringBuffer(fromDate);
	       String yearFrom = sBuffer.substring(0,4);
	       String monFrom = sBuffer.substring(5,7);
	       String ddFrom = sBuffer.substring(8,10);
	       int intYearFrom = Integer.parseInt(yearFrom);
	       int intMonFrom = Integer.parseInt(monFrom);
	       int intDdFrom = Integer.parseInt(ddFrom);

	       // set the fromDate in java.util.Calendar
	       cal1.set(intYearFrom, intMonFrom, intDdFrom);

	       //split year, month and days from the date using StringBuffer.
	       StringBuffer sBuffer1 = new StringBuffer(toDate);
	       String yearTo = sBuffer1.substring(0,4);
	       String monTo = sBuffer1.substring(5,7);
	       String ddTo = sBuffer1.substring(8,10);
	       int intYearTo = Integer.parseInt(yearTo);
	       int intMonTo = Integer.parseInt(monTo);
	       int intDdTo = Integer.parseInt(ddTo);

	       // set the toDate in java.util.Calendar
	       cal2.set(intYearTo, intMonTo, intDdTo);

	       //call method daysBetween to get the number of days between two dates
	       int days = daysBetween(cal1.getTime(),cal2.getTime());
	       return days;
	   }

	   //This method is called by the above method numberOfDays
	   public int daysBetween(Date d1, Date d2)
	   {
	      return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	   }
	   
}
