package com.jboss.soap.service.impl;


import java.util.List;

import javax.jws.WebService;

import com.jboss.soap.service.AcmeDemoInterface;
import com.jboss.soap.service.Flight;
import com.jboss.soap.service.FlightRequest;

@WebService(serviceName = "AcmeDemoService", endpointInterface = "com.jboss.soap.service.AcmeDemoInterface", targetNamespace = "http://service.soap.jboss.com/AcmeDemo/")
public class AcmeDemoInterfaceImpl implements AcmeDemoInterface  {
	
	private FlightAccess flightAccess;
	
	@Override
	public List<Flight> listAvailablePlanes(FlightRequest in) {
		// TODO Auto-generated method stub
		return flightAccess.listAvailablePlanes(in);
	}
}