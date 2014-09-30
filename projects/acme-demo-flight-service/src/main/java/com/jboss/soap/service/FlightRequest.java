package com.jboss.soap.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "flightRequest", propOrder = {
    "startCity",
    "endCity",
    "startDate",
    "endDate"
})
public class FlightRequest implements Serializable {
	
	@XmlElement(required = true)
    protected String startCity;
	@XmlElement(required = true)
    protected String endCity;
    @XmlElement(required = true)
    protected String startDate;
    @XmlElement(required = true)
    protected String endDate;
	public String getStartCity() {
		return startCity;
	}
	public void setStartCity(String value) {
		this.startCity = value;
	}
	public String getEndCity() {
		return endCity;
	}
	public void setEndCity(String value) {
		this.endCity = value;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String value) {
		this.startDate = value;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String value) {
		this.endDate = value;
	}

	}
