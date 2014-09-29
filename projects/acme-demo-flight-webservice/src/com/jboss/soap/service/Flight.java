
package com.jboss.soap.service;

import com.jboss.soap.service.acmedemo.impl.adapters.Adapter1;
import com.jboss.soap.service.acmedemo.impl.adapters.Adapter2;

import java.math.BigDecimal; 
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for flight complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="flight">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="company" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *         &lt;element name="planeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ratePerPerson" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="startCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="targetCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *         &lt;element name="travelDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Entity
@Table(name="plane",catalog="wsplane")
@NamedQueries({  
    @NamedQuery(name = "Flight.findAll", query = "SELECT f FROM Flight f"),
    @NamedQuery(name = "Flight.findFiltered", 
	query = "SELECT f FROM Flight f WHERE f.startCity = :startCity AND f.targetCity = :targetCity AND f.travelDate = :travelDate")   })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "flight", propOrder = {
    "company",
    "endTime",
    "planeId",
    "ratePerPerson",
    "startCity",
    "targetCity",
    "startTime",
    "travelDate"
})
public class Flight implements java.io.Serializable {

    @XmlElement(required = true)
    protected String company;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "time")
    protected Date endTime;
    protected int planeId;
    @XmlElement(required = true)
    protected BigDecimal ratePerPerson;
    @XmlElement(required = true)
    protected String startCity;
    @XmlElement(required = true)
    protected String targetCity;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "time")
    protected Date startTime;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "date")
    protected Date travelDate;

    /**
     * Gets the value of the company property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Column(name="company", length=45)
    public String getCompany() {
        return company;
    }

    /**
     * Sets the value of the company property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompany(String value) {
        this.company = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Temporal(TemporalType.TIME)
    @Column(name="end_time", length=8)
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndTime(Date value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the planeId property.
     * 
     */
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="flight_id", unique=true, nullable=false)
    public int getPlaneId() {
        return planeId;
    }

    /**
     * Sets the value of the planeId property.
     * 
     */
    public void setPlaneId(int value) {
        this.planeId = value;
    }

    /**
     * Gets the value of the ratePerPerson property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    @Column(name="rate_per_person", precision=10)
    public BigDecimal getRatePerPerson() {
        return ratePerPerson;
    }

    /**
     * Sets the value of the ratePerPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRatePerPerson(BigDecimal value) {
        this.ratePerPerson = value;
    }

    /**
     * Gets the value of the startCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Column(name="start_city", nullable=false, length=45)
    public String getStartCity() {
        return startCity;
    }

    /**
     * Sets the value of the startCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartCity(String value) {
        this.startCity = value;
    }

    /**
     * Gets the value of the targetCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Column(name="target_city", nullable=false, length=45)
    public String getTargetCity() {
        return targetCity;
    }

    /**
     * Sets the value of the targetCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetCity(String value) {
        this.targetCity = value;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Temporal(TemporalType.TIME)
    @Column(name="start_time", length=8)
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartTime(Date value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the travelDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Temporal(TemporalType.DATE)
    @Column(name="travel_date", length=8)
    public Date getTravelDate() {
        return travelDate;
    }

    /**
     * Sets the value of the travelDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTravelDate(Date value) {
        this.travelDate = value;
    }

}
