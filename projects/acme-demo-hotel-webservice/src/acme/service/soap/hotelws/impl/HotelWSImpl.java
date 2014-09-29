package acme.service.soap.hotelws.impl;


import javax.ejb.EJB;
import javax.jws.WebService;

import acme.service.soap.hotelws.HotelRequest;
import acme.service.soap.hotelws.HotelWS;
import acme.service.soap.hotelws.Resort;
import acme.service.soap.hotelws.exception.DataBaseException;


@WebService(serviceName = "HotelWS", endpointInterface = "acme.service.soap.hotelws.HotelWS", targetNamespace = "http://soap.service.acme/HotelWS/")
public class HotelWSImpl implements HotelWS {
	
	@EJB
	private HotelAccess hotelAccess;
	
	public Resort getAvailableHotel(HotelRequest in) throws DataBaseException {
		
		return hotelAccess.getAvailableHotel(in);
	}
}