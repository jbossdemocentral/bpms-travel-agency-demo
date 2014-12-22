package org.jboss.jbpm.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name = "task")
public class Task {
	private String id;
	private String name;
    private String subject;
    private String description;
    private String status;
    private String priority;
    private String skipable;
    private String actualOwner;
    private String createdBy;
    private String createdOn;
    private String activationTime;
    private String processInstanceId;
    private String processId;
    private String processSessionId;
    private String subTaskStrategy;
    private String parentId;
    
    @XmlElement public String getId(){ return id; }
    @XmlElement public String getName(){ return name; }
    @XmlElement public String getSubject(){ return subject; }
    @XmlElement public String getDescription(){ return description; }
    @XmlElement public String getStatus(){ return status; }
    @XmlElement public String getPrority(){ return priority; }
    @XmlElement public String getSkipable(){ return skipable; }
    @XmlElement public String getActualOwner(){ return actualOwner; }
    @XmlElement public String getCreatedBy(){ return createdBy; }
    @XmlElement public String getCreatedOn(){ return createdOn; }
    @XmlElement public String getActivationTime(){ return activationTime; }
    @XmlElement public String getProcessInstanceId(){ return processInstanceId; }
    @XmlElement public String getProcessId(){ return processId; }
    @XmlElement public String getProcessSessionId(){ return processSessionId; }
    @XmlElement public String getSubTaskStrategy(){ return subTaskStrategy; }
    @XmlElement public String getParentId(){ return parentId; }
	
    public Task(String id, String name, String subject, String description, String status, String priority, String skipable, String actualOwner,
			String createdBy, String createdOn, String activationTime, String processInstanceId, String processId, String processSessionId,
			String subTaskStrategy, String parentId) {
		super();
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.description = description;
		this.status = status;
		this.priority = priority;
		this.skipable = skipable;
		this.actualOwner = actualOwner;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.activationTime = activationTime;
		this.processInstanceId = processInstanceId;
		this.processId = processId;
		this.processSessionId = processSessionId;
		this.subTaskStrategy = subTaskStrategy;
		this.parentId = parentId;
	}
    
    public String toString(){
    	return ToStringBuilder.reflectionToString(this);
    }
    
}
