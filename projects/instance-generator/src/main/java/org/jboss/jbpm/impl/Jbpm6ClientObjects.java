package org.jboss.jbpm.impl;

import static com.google.common.collect.Lists.newArrayList;
import static javax.xml.xpath.XPathConstants.*;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.apache.http.HttpException;
import org.jboss.jbpm.api.Response;
import org.jboss.jbpm.api.Task;
import org.jboss.jbpm.api.Jbpm6Client.TasksBy;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Same as Jbpm6ClientImpl but returns parsed objects instead
 * 
 * @author mallen@redhat.com
 *
 */
public class Jbpm6ClientObjects {
  private Jbpm6ClientImpl client;

  public Jbpm6ClientObjects(String serverUrl, String username, String password) {
    client=new Jbpm6ClientImpl(serverUrl, username, password, true);
  }

  public List<Task> getTasks(TasksBy by, String value) throws HttpException {
    try {
      String responseString=client.getTasks(by,value);
      Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(responseString.getBytes()));
      XPath xpath=XPathFactory.newInstance().newXPath();
      List<Task> tasks=newArrayList();

      Double count=(Double) xpath.evaluate("count(/task-summary-list/task-summary)",doc,NUMBER);
      for (int i=1; i <= count; i++) {
        Node node=(Node) xpath.evaluate("/task-summary-list/task-summary[" + i + "]",doc,NODE);
        String id=(String) xpath.evaluate("id",node,STRING);
        String name=(String) xpath.evaluate("name",node,STRING);
        String subject=(String) xpath.evaluate("subject",node,STRING);
        String description=(String) xpath.evaluate("description",node,STRING);
        String status=(String) xpath.evaluate("status",node,STRING);
        String priority=(String) xpath.evaluate("priority",node,STRING);
        String skipable=(String) xpath.evaluate("skipable",node,STRING);
        String actualOwner=(String) xpath.evaluate("actual-owner",node,STRING);
        String createdBy=(String) xpath.evaluate("created-by",node,STRING);
        String createdOn=(String) xpath.evaluate("created-on",node,STRING);
        String activationTime=(String) xpath.evaluate("activation-time",node,STRING);
        String processInstanceId=(String) xpath.evaluate("process-instance-id",node,STRING);
        String processId=(String) xpath.evaluate("process-id",node,STRING);
        String processSessionId=(String) xpath.evaluate("process-session-id",node,STRING);
        String subTaskStrategy=(String) xpath.evaluate("sub-task-strategy",node,STRING);
        tasks.add(new Task(id, name, subject, description, status, priority, skipable, actualOwner, createdBy, createdOn, activationTime, processInstanceId, processId, processSessionId, subTaskStrategy, "-1"));
      }
      return tasks;
    } catch (Exception e) {
      throw new HttpException(e.getMessage(), e);
    }
  }

  public Response startTask(String id) throws HttpException {
    try {
      String responseString=client.startTask(id);
      Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(responseString.getBytes()));
      XPath xpath=XPathFactory.newInstance().newXPath();

      String status=(String) xpath.evaluate("/response/status",doc,STRING);
      String url=(String) xpath.evaluate("/response/url",doc,STRING);

      return new Response(status, url);
      // <response><status>SUCCESS</status><url>/business-central/rest/task/5/start</url></response>
    } catch (Exception e) {
      throw new HttpException(e.getMessage(), e);
    }
  }


}
