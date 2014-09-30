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
		
		
			hotel.setHotelId(395);
			hotel.setHotelName("Holiday Inn");
			hotel.setRatePerPerson(200);
			hotel.setHotelCity(hotelCity);
			hotel.setAvailableFrom(in.getStartDate());
			hotel.setAvailableTo(in.getEndDate());
			
		return hotel;
	}
}