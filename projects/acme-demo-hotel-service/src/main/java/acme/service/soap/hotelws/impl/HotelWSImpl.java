package acme.service.soap.hotelws.impl;


import java.util.Random;

import javax.jws.WebService;

import acme.service.soap.hotelws.HotelRequest;
import acme.service.soap.hotelws.HotelWS;
import acme.service.soap.hotelws.Resort;

@WebService(serviceName = "HotelWS", endpointInterface = "acme.service.soap.hotelws.HotelWS", targetNamespace = "http://soap.service.acme/HotelWS/")
public class HotelWSImpl implements HotelWS {
	public String bookHotel(String in) {
		System.out.println("YOUR BOOKING HAS BEEN SUCCESSFUL");
		
		SessionIdentifierGenerator bookingRef = new SessionIdentifierGenerator();
		String refNum = bookingRef.nextSessionId();
		
		System.out.println("YOUR BOOKING REFERENCE IS: "+ refNum);
		
		return refNum;
	}

	public int cancelBooking(String in) {
		int cancelCharge = 0;
		final Random random = new Random();
		
		if (in == null)
			throw new IllegalArgumentException("No booking found");
		
		cancelCharge = random.nextInt((10-5)+1) + 5;
		
		return cancelCharge;
	}

	public Resort getAvailableHotel(HotelRequest in) {
		Resort hotel = new Resort();
		
		String hotelCity = in.getTargetCity();	
		
			hotel.setHotelId(395);
			hotel.setHotelName("Holiday Inn");
			hotel.setRatePerPerson(200);
			hotel.setHotelCity(hotelCity);
			hotel.setAvailableFrom(in.getStartDate());
			hotel.setAvailableTo(in.getEndDate());
			
			return hotel;
	}
}