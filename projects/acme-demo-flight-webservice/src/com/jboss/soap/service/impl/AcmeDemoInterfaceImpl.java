package com.jboss.soap.service.impl;


import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;

import com.jboss.soap.service.AcmeDemoInterface;
import com.jboss.soap.service.Flight;
import com.jboss.soap.service.FlightRequest;
import com.jboss.soap.service.exception.DataBaseException;

@WebService(serviceName = "AcmeDemoService", endpointInterface = "com.jboss.soap.service.AcmeDemoInterface", targetNamespace = "http://service.soap.jboss.com/AcmeDemo/")
public class AcmeDemoInterfaceImpl implements AcmeDemoInterface  {
	
	@EJB
	private FlightAccess flightAccess;
	
	@Override
	public List<Flight> listAvailablePlanes(FlightRequest in)
			throws DataBaseException {
		// TODO Auto-generated method stub
		return flightAccess.listAvailablePlanes(in);
	}
}