package com.jboss.soap.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import com.jboss.soap.service.AcmeDemoInterface;
import com.jboss.soap.service.Flight;
import com.jboss.soap.service.FlightRequest;

@WebService(serviceName = "AcmeDemoService", endpointInterface = "com.jboss.soap.service.AcmeDemoInterface", targetNamespace = "http://service.soap.jboss.com/AcmeDemo/")
public class AcmeDemoInterfaceImpl implements AcmeDemoInterface  {
	
	
	public Flight listAvailablePlanes(FlightRequest in) {
		// TODO Auto-generated method stub
		String startCity = in.getStartCity();
		String endCity = in.getEndCity();
		BigDecimal outboundBD = new BigDecimal(240);
		BigDecimal inboundBD = new BigDecimal(119);
		
	
		
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
}