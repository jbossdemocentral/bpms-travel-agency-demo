package org.jboss.jbpm.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name = "response")
public class Response {
	private String status;
	private String url;
	public Response(String status, String url) {
		super();
		this.status = status;
		this.url = url;
	}
	
	@XmlElement public String getStatus(){ return status; }
	@XmlElement public String getUrl(){ return url; }
	
	public String toString(){
    	return ToStringBuilder.reflectionToString(this);
    }
	
}
