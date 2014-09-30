package acme.service.soap.hotelws.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;

import acme.service.soap.hotelws.HotelRequest;
import acme.service.soap.hotelws.HotelWS;
import acme.service.soap.hotelws.Resort;


@WebService(serviceName = "HotelWS", endpointInterface = "acme.service.soap.hotelws.HotelWS", targetNamespace = "http://soap.service.acme/HotelWS/")
public class HotelWSImpl implements HotelWS {
	
	
	public Resort getAvailableHotel(HotelRequest in)  {
		Resort hotel = new Resort();
		
		String hotelCity = in.getTargetCity();	
		
		BigDecimal priceBD = new BigDecimal(200);
		
		DateCalc obj = new DateCalc();
		
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date startDate1 = null;
		Date endDate1 = null;
		
		
			try {
				startDate1 = formatter.parse(in.getStartDate());
				System.out.println("Date in text format: " + in.getStartDate());
	            System.out.println("Date in java.util.Date Obj : " + startDate1);
			} catch (java.text.ParseException e) {
			
				e.printStackTrace();
			}
			
			try {
				endDate1 = formatter.parse(in.getEndDate());
				System.out.println("Date in text format: " + in.getEndDate());
	            System.out.println("Date in java.util.Date Obj : " + endDate1);
			} catch (java.text.ParseException e) {
				
				e.printStackTrace();
			}
			
			int duration =  obj.numberOfDays(in.getStartDate(), in.getEndDate()) ;
			
			hotel.setHotelId(395);
			hotel.setHotelName("Holiday Inn");
			hotel.setRatePerPerson(200);
			hotel.setHotelCity(hotelCity);
			hotel.setTargetDate(startDate1);
			hotel.setBreakfastIncluded(true);
			hotel.setRoomType("Double Room");
			hotel.setHolDuration(duration);
			
		return hotel;
	}
}