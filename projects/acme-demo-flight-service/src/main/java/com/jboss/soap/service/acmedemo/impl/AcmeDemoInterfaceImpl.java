package com.jboss.soap.service.acmedemo.impl;


import java.math.BigDecimal;
import java.util.Random;

import javax.jws.WebService;

import com.jboss.soap.service.acmedemo.AcmeDemoInterface;
import com.jboss.soap.service.acmedemo.Flight;
import com.jboss.soap.service.acmedemo.FlightRequest;

@WebService(serviceName = "AcmeDemoService", endpointInterface = "com.jboss.soap.service.acmedemo.AcmeDemoInterface", targetNamespace = "http://service.soap.jboss.com/AcmeDemo/")
public class AcmeDemoInterfaceImpl implements AcmeDemoInterface {
	public Flight listAvailablePlanes(FlightRequest in) {
		// TODO Auto-generated method stub
		String startCity = in.getStartCity();
		String endCity = in.getEndCity();
		BigDecimal outboundBD = new BigDecimal(525);
//		BigDecimal inboundBD = new BigDecimal(119);
		
	
		
		Flight outbound = new Flight();
		outbound.setCompany("EasyJet");
		outbound.setPlaneId(12345);
		outbound.setRatePerPerson(outboundBD);
		outbound.setStartCity(startCity);
		outbound.setTargetCity(endCity);
		outbound.setTravelDate(in.getStartDate());
		
		System.out.println("OUTBOUND FLIGHT variables set");
		
//		Flight inbound = new Flight();
//		inbound.setCompany("EasyJet");
//		inbound.setPlaneId(12345);
//		inbound.setRatePerPerson(inboundBD);
//		inbound.setStartCity(endCity);
//		inbound.setTargetCity(startCity);
//		inbound.setTravelDate(in.getEndDate());
//		
//		System.out.println("INBOUND FLIGHT variables set");
//		
//		
//		List<Flight> itinery = new ArrayList<Flight>();
//		itinery.add(outbound);
		
		return outbound; 
	}

	public String bookFlights(String in) {
		System.out.println("SUCCESS: Your flights are now reserved.");
		System.out.println();
		
		SessionIdentifierGenerator bookingRef = new SessionIdentifierGenerator();
		String refNum = bookingRef.nextSessionId();
		
		System.out.println("Your RESERVATION NUMBER is: "+ refNum);
		
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
}