package com.jboss.soap.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.jboss.soap.service.Flight;
import com.jboss.soap.service.FlightRequest;

public class FlightAccess {


	public List<Flight> listAvailablePlanes(FlightRequest in) {
				
		String startCity = in.getStartCity();
		String endCity = in.getEndCity();
		BigDecimal outboundBD = new BigDecimal(240);
		BigDecimal inboundBD = new BigDecimal(119);
		
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date dateFlyingOut = null;
		Date dateFlyingBack = null;
		
			try {
				dateFlyingOut = formatter.parse(in.getStartDate());
				System.out.println("Date in text format: " + in.getStartDate());
	            System.out.println("Date in java.util.Date Obj : " + dateFlyingOut);
			} catch (java.text.ParseException e) {
			
				e.printStackTrace();
			}
				
		
			try {
				dateFlyingBack = formatter.parse(in.getEndDate());
				System.out.println("Date in text format: " + in.getEndDate());
	            System.out.println("Date in java.util.Date Obj : " + dateFlyingBack);
			} catch (java.text.ParseException e) {
				
				e.printStackTrace();
			}
		
		
		Flight outbound = new Flight();
		outbound.setCompany("EasyJet");
		outbound.setEndTime(dateFlyingBack);
		outbound.setPlaneId(12345);
		outbound.setRatePerPerson(outboundBD);
		outbound.setStartCity(startCity);
		outbound.setStartTime(dateFlyingOut);
		outbound.setTargetCity(endCity);
		outbound.setTravelDate(dateFlyingOut);
		
		System.out.println("OUTBOUND FLIGHT variables set");
		
		Flight inbound = new Flight();
		inbound.setCompany("EasyJet");
		inbound.setEndTime(dateFlyingOut);
		inbound.setPlaneId(12345);
		inbound.setRatePerPerson(inboundBD);
		inbound.setStartCity(endCity);
		inbound.setStartTime(dateFlyingBack);
		inbound.setTargetCity(startCity);
		inbound.setTravelDate(dateFlyingBack);
		
		System.out.println("INBOUND FLIGHT variables set");
		
		
		List<Flight> itinery = new ArrayList<Flight>();
		itinery.add(outbound);
		
		return itinery; 

	
	}
}
