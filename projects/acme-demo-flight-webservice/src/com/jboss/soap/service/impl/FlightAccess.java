package com.jboss.soap.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import com.jboss.soap.service.Flight;
import com.jboss.soap.service.FlightRequest;
import com.jboss.soap.service.exception.DataBaseException;

@Stateless
@LocalBean
public class FlightAccess {
	 
	
	@PersistenceContext
	private EntityManager em;

	public List<Flight> listAvailablePlanes(FlightRequest in) throws DataBaseException {
		
		
		String startCity = in.getStartCity();
		String endCity = in.getEndCity();
		
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
		
		
		try {
			
		em.setProperty("javax.persistance.cache.retrieveMode", "BYPASS");
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> query1 = cb.createQuery(Flight.class);
		Root<Flight> outFlight = query1.from(Flight.class);
			
		query1.select(outFlight)
			.where(cb.equal(outFlight.get("startCity"), startCity),
				cb.equal(outFlight.get("targetCity"), endCity),
				cb.equal(outFlight.get("travelDate"), dateFlyingOut));
			
		Flight outbound = em.createQuery(query1).getSingleResult();
		
		
		System.out.println("OUTBOUND FLIGHT criteria query says : " + outbound);	
		
		em.setProperty("javax.persistance.cache.retrieveMode", "BYPASS");
		CriteriaBuilder cab = em.getCriteriaBuilder();
		CriteriaQuery<Flight> query2 = cab.createQuery(Flight.class);
		Root<Flight> inFlight = query2.from(Flight.class);
		
		query2.select(inFlight)
			.where(cb.equal(inFlight.get("startCity"), endCity),
				cb.equal(inFlight.get("targetCity"), startCity),
				cb.equal(inFlight.get("travelDate"), dateFlyingBack));
		
		Flight inbound = em.createQuery(query2).getSingleResult();
		
		System.out.println("RETURN FLIGHT criteria query returned : " + inbound);
		
		List<Flight> itinery = new ArrayList<Flight>();
		itinery.add(outbound);
		itinery.add(inbound);
		
		return itinery;
		
	}  catch (HibernateException v_Exception) {
		
		throw new DataBaseException(v_Exception);
	} 

	
	}
}
