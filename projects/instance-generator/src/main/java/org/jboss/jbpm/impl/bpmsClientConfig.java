package org.jboss.jbpm.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.lang.text.StrSubstitutor;
import org.jboss.jbpm.impl.bpmsClientVars;


public class bpmsClientConfig {

    public    String server = "http://localhost:8080/business-central/";
    public    String username = "erics";
    public    String password = "bpmsuite1!";

    public   Number MAX_TASK_DELAY;
    public   Number  MAX_PRICE;
    public   Number  MAX_INSTANCES;

    public ArrayList<String> NofTChoice = new ArrayList<String>();
    public ArrayList<String> BooleanChoice = new ArrayList<String>();
    public ArrayList<String> NameChoice = new ArrayList<String>();
    public Map vars = new HashMap();
    public Map taskDefs = new HashMap();
    public Map processStart = new HashMap();
    public JSONArray processParamDefs;


    public String projectID;
    public String processName;


    private static final String DefaultFilePath = "/home/aubbiali/IdeaProjects/jbpm6-process-client/bpmsClient.json";

    public String getVarValue(String name) {

        bpmsClientVars v = (bpmsClientVars) vars.get(name);
        if (v==null)
                return ("NULL");

        return v.getValue();

    }

    public bpmsClientConfig () {

    }

    public Map generateParamValues(JSONArray paramDefs) {
        Map values = new HashMap();

        for (int i=0; i < paramDefs.size(); i++) {
            JSONObject jo = (JSONObject) paramDefs.get(i);
            String s= (String) jo.get("value");

            if (s.contains("${")) {
                s = s.substring(s.indexOf("${") +2,s.indexOf("}"));
                bpmsClientVars v = (bpmsClientVars)vars.get(s);
                values.put(s, v.getValue() );
            }

        }
        return values;
    }
    public Map generateProcessParamValues() {

        return generateParamValues(processParamDefs);
    }

    public Map generateParams(Map vars,JSONArray paramDefs ){
        StrSubstitutor sub = new StrSubstitutor(vars);
        String templateString;
        JSONObject jo;
        Map returnParams = new HashMap();
        for (int i=0; i< paramDefs.size(); i++ ) {
            jo = (JSONObject) paramDefs.get(i);
            templateString = (String) jo.get("value");
            String resolvedString = sub.replace(templateString);
            returnParams.put((String) jo.get("name"), resolvedString);

        }
        return returnParams;
    }

    public Map generateProcessParams(Map processVars){

        return generateParams(processVars,processParamDefs );
   }


    public Map generateTaskParams(String taskName){

        JSONObject jo = (JSONObject) taskDefs.get(taskName);
        JSONArray  taskParamsDefs = (JSONArray) jo.get("outParams");
        Map vars = generateParamValues(taskParamsDefs);

        return generateParams(vars,taskParamsDefs );
    }


    public void parseJSON (String filePath) {

    try {
              if (filePath==null)
                  filePath = DefaultFilePath;
                else
                   if (filePath == "")
                        filePath = DefaultFilePath;

              // read the json file
              FileReader reader = new FileReader(filePath);

              JSONParser jsonParser = new JSONParser();
              JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

              // get a String from the JSON object
              server = (String) jsonObject.get("url");
              System.out.println("Server: " + server);
              password = (String) jsonObject.get("password");
              System.out.println("password: " + password);
              username = (String) jsonObject.get("username");
              System.out.println("username: " + username);


              MAX_TASK_DELAY  =  (Number) jsonObject.get("max_task_delay");
              System.out.println("The max task delay  is: " + MAX_TASK_DELAY);

              MAX_INSTANCES  =  (Number) jsonObject.get("n_instances");
              System.out.println("The number of instances is: " + MAX_INSTANCES);

              MAX_PRICE  =  (Number) jsonObject.get("price");
              System.out.println("The max price  is: " + MAX_PRICE);


              JSONArray arr = (JSONArray) jsonObject.get("names");
              for (int i = 0; i < arr.size(); i++) {
                NameChoice.add ((String) arr.get(i).toString());
                System.out.println(NameChoice.get(i));
            }
             arr = (JSONArray) jsonObject.get("review");
             for (int i = 0; i < arr.size(); i++) {
                BooleanChoice.add ((String) arr.get(i).toString());
                System.out.println(BooleanChoice.get(i));
            }


            arr = (JSONArray) jsonObject.get("travellers");
            for (int i = 0; i < arr.size(); i++) {
                NofTChoice.add ((String) arr.get(i).toString());
                System.out.println(NofTChoice.get(i));
            }


           try {
               arr = (JSONArray) jsonObject.get("vars");
               for (int i = 0; i < arr.size(); i++) {
                   bpmsClientVars v = new bpmsClientVars();
                   JSONObject jo = (JSONObject) arr.get(i);
                   v.name = (String) jo.get("name");
                   v.type = (String) jo.get("type");
                   if (v.type.equals("int")) {
                       JSONObject jo1 = (JSONObject) jo.get("value");
                       v.min = (Number) jo1.get("min");
                       v.max = (Number) jo1.get("max");
                   } else if (v.type.equals("choice")) {
                       JSONArray arr1 = (JSONArray) jo.get("value");
                       for (int j = 0; j < arr1.size(); j++) {
                           v.choice.add((String) arr1.get(j).toString());
                       }
                   }
                   vars.put(v.name,v);
               }
           } catch (Exception e) {

               e.printStackTrace();
           }


        try {
            arr = (JSONArray) jsonObject.get("tasks");
            for (int i = 0; i < arr.size(); i++) {

                Map task = (JSONObject) arr.get(i);
                String name = (String) task.get("name");
                taskDefs.put(name, task);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        try {
            processStart = (JSONObject) jsonObject.get("process");
            projectID = (String) processStart.get("project");
            processName = (String) processStart.get ("name");
            processParamDefs = (JSONArray) processStart.get("params");

        } catch (Exception e) {

            e.printStackTrace();
        }


 //           vars.add ((String) arr.get(i).toString());
 //           System.out.println(NofTChoice.get(i));
 //           }

/*
              // get an array from the JSON object
              JSONArray lang= (JSONArray) jsonObject.get("languages");

              // take the elements of the json array
              for(int i=0; i<lang.size(); i++){
                      System.out.println("The " + i + " element of the array: "+lang.get(i));
                  }
              Iterator i = lang.iterator();


                  // take each value from the json array separately
                  while (i.hasNext()) {
                      JSONObject innerObj = (JSONObject) i.next();
                      System.out.println("language "+ innerObj.get("lang") +
                              " with level " + innerObj.get("knowledge"));
                  }
                  // handle a structure into the json object
                  JSONObject structure = (JSONObject) jsonObject.get("job");
                  System.out.println("Into job structure, name: " + structure.get("name"));
*/
              } catch (FileNotFoundException ex) {
                  ex.printStackTrace();
              } catch (IOException ex) {
                  ex.printStackTrace();
              } catch (ParseException ex) {
                  ex.printStackTrace();
              } catch (NullPointerException ex) {
                  ex.printStackTrace();
              }
    }
}