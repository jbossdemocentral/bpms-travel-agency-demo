package acme.service.soap.hotelws.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;

import acme.service.soap.hotelws.HotelRequest;
import acme.service.soap.hotelws.Resort;
import acme.service.soap.hotelws.exception.DataBaseException;

@Stateless
@LocalBean
public class HotelAccess {
	 
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("deprecation")
	public Resort getAvailableHotel(HotelRequest in) throws DataBaseException {
			
		String hotelCity = in.getTargetCity();	
		
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
		
	try {
		
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Resort> query = cb.createQuery(Resort.class);
		Root<Resort> resort = query.from(Resort.class);
		
		
		query.where(
				cb.equal(resort.get("hotelCity"), hotelCity),
				cb.equal(resort.get("targetDate"), startDate1)
				);

		Resort hotel = em.createQuery(query).getSingleResult();
		
		hotel.setHolDuration(duration);
		
		
		return hotel;
		
		}  catch (HibernateException v_Exception) {
		
		throw new DataBaseException(v_Exception);
	} 

}
}

	


