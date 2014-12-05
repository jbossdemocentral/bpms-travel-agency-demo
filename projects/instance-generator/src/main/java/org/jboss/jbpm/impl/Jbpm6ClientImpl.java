package org.jboss.jbpm.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.http.HttpException;
import org.jboss.jbpm.api.Jbpm6Client;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.jboss.jbpm.impl.Jbpm6ClientImpl.Http.GET;
import static org.jboss.jbpm.impl.Jbpm6ClientImpl.Http.POST;

/**
 * Simple rest client to interact with JBPM6s REST API for process management
 * 
 * @author mallen@redhat.com
 *
 */
public class Jbpm6ClientImpl implements Jbpm6Client{
	private final String server;
	private final String username;
	private final String password;
	private final boolean debug;
	public enum Http{GET,POST}
	
	public Jbpm6ClientImpl(String serverUrl, String username, String password, boolean debug){
		Preconditions.checkArgument(serverUrl!=null);
		Preconditions.checkArgument(serverUrl.endsWith("/"), "the serverUrl must end with a / character");
		this.server=serverUrl;
		this.username=username;
		this.password=password;
		this.debug=debug;
	}
	
  public String getTasks(TasksBy by, String value) throws HttpException{
    return send(GET, "rest/task/query?"+by.name()+"="+value);
  }
	
	
	public String startProcess(String deploymentId, String processId, String mapOfParams) throws HttpException{
		return startProcessWithMap(deploymentId, processId, queryStringToMap(mapOfParams));
	}
	public  String startProcessWithMap(String deploymentId, String processId, Map<String,Object> params) throws HttpException{
		Preconditions.checkArgument(deploymentId.split(":").length==3);
		return send(POST, "rest/runtime/"+deploymentId+"/process/"+processId+"/start"+mapToQueryString(params));
	}
	
	
	public String startTask(String id) throws HttpException{
		return send(POST, "rest/task/"+id+"/start");
	}

    public String claimTask(String id) throws HttpException{
        return send(POST, "rest/task/"+id+"/claim");
    }

	public String completeTask(String id, String commaSeparatedListOfParams) throws HttpException{
		return completeTaskWithMap(id, queryStringToMap(commaSeparatedListOfParams));
	}
	public String completeTaskWithMap(String id, Map<String, Object> params) throws HttpException{
		return send(POST, "rest/task/"+id+"/complete"+mapToQueryString(params));
	}
	public String getTaskContent (String id) throws HttpException {

        return send(GET, "rest/task/"+id+"/content");
    }

	private Response send(String url, Http httpType){
		RequestSpecification rs=
				given().redirects().follow(true)
				.auth().preemptive().basic(username,password)
				.when();
		Response response;
		switch (httpType){
			case POST:response = rs.post(server+url); break;
			case GET:response  = rs.get (server+url); break;
			default:response   = rs.get (server+url);
		}
		return response;
	}
	
	
	private String send(Http httpType, String url) throws HttpException{
		Response response=send(url, httpType);
		if (response.getStatusCode()!=200)
			throw new HttpException("Failed to "+httpType.name()+" to "+url+" - http status line = "+ response.getStatusLine() +"; response content = "+ response.asString());
		
		// add the status line for info/debugging purposes
		String result=response.asString();
		if (debug){
		  result=new StringBuffer(result)
  		.insert(result.indexOf(">")+1, "<!-- "+response.getStatusLine()+" -->")
  		.toString();
		}

        System.out.println("Send response:" + result); // Display the string.
		return result;

	}

	/**
	 * Convert a Map object into the jbpm6 queryString equivalent of a map of values but prepending the text "map_"
	 */
	private String mapToQueryString(Map<String,Object> params){
		StringBuffer sb=new StringBuffer();
		for(Map.Entry<String, Object> e:params.entrySet()){
			sb.append("&map_"+e.getKey()+"="+e.getValue().toString());
      if (Integer.class.isAssignableFrom(e.getValue().getClass()))
        sb.append("i"); // force the Integer data type once its been received by Jbpm server
      if (Long.class.isAssignableFrom(e.getValue().getClass()))
        sb.append("l"); // force the Long data type once its been received by Jbpm server
		}
		if (sb.length()>0)sb.replace(0,1,"?");
		return sb.toString();
	}

	public static Map<String, Object> queryStringToMap(String queryString) {
	    Map<String, Object> result = Maps.newLinkedHashMap();
	    String[] pairs = queryString.split(",");
	    for (String pair : pairs) {
	    	String[] keyValue=pair.split("=");
	    	if (keyValue.length==2){
	    		result.put(keyValue[0], keyValue[1]);
	    	}
//	        int idx = pair.indexOf("=");/
//	        result.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return result;
	}

}
