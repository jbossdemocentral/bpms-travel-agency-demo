package org.jboss.jbpm;

/**
 * Created by aubbiali on 05/11/14.
 */

import org.apache.http.HttpException;
import org.jboss.jbpm.api.Jbpm6Client.TasksBy;
import org.jboss.jbpm.api.Task;
import org.jboss.jbpm.impl.Jbpm6ClientImpl;
import org.jboss.jbpm.impl.Jbpm6ClientObjects;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static javax.xml.xpath.XPathConstants.STRING;


public class bpmsClientApp {

    private static final String server = "http://localhost:8080/business-central/";
    private static final String username = "erics";
    private static final String password = "bpmsuite1!";
    private static Jbpm6ClientImpl client;
    private static Jbpm6ClientObjects clientObjects;

    private String processid = null;

    private static String BooleanChoice[] = {"False","False","True"};
    private static String NameChoice[] =  {"Kiara Z. Hanson",
            "Jordan X. May",
            "Castor Lewis",
            "Ainsley B. Griffin",
            "Yael W. Dotson",
            "Cyrus Burke",
            "Martina L. Palmer",
            "Lareina Castro",
            "Leroy F. Blanchard",
            "Jared Middleton",
            "Kirby Hayden",
            "Rinah Anthony",
            "Aquila E. Howell",
            "Jennifer Ortiz",
            "Macaulay H. Morrison",
            "Shellie Merrill",
            "Jarrod Estrada",
            "Ursula Hines",
            "Armando P. Ruiz",
            "Kirestin W. Head",
            "Pamela D. Hammond",
            "Dawn M. Kane"};

    private String NofTChoice[] = {"1i","2i","3i","4i"};

    private List<Task> tasks = null;

    private String rndChoice(String [] a) {
        int idx = new Random().nextInt(a.length);
        return a[idx];
    }

    private Task taskReady (List<Task> tasks) {

        if (tasks == null) return null;



        for(Task task_i : tasks) {

            if (task_i.getStatus().contains("Ready"))  {

                return task_i;

            }

        }
        return null;
    }




    private void init() {
        client = new Jbpm6ClientImpl(server, username, password, true);
        clientObjects = new Jbpm6ClientObjects(server, username, password);
    }

    private Task executeNextTask (int retries) {

        tasks = null;
        Task task = null;
        String response;

        for (int i= 0; i<retries; i++){
            try {
                tasks = clientObjects.getTasks(TasksBy.processInstanceId, processid);

            } catch (HttpException e) {
                e.printStackTrace();
            }

            task = taskReady(tasks);

            if (task != null) break;
        }

        if (task == null)
                return (task);



        System.out.println("completing task " + task.getName()+ " "+task.getId()+  ": " + task.getDescription()+task.getStatus());
        try {
                response = client.claimTask(task.getId());
                response = client.startTask(task.getId());
                if (task.getName().contains("Price Review")) {
                    Integer price = new Random().nextInt(5000);
                    String priceString = price.toString() + "i";
                    response = client.completeTask(task.getId(),
                            "totalPriceOut=" + priceString
                                    + ",reviewerCommentOut=No Comments");
                }

                else if (task.getName().contains("Booking Complete")) {
                    response = client.completeTask(task.getId(),
                            "");
                }

                else if (task.getName().contains("Employee Booking")) {
                    response = client.completeTask(task.getId(),
                        "reviewRequiredOut="+ rndChoice(BooleanChoice)
                        +",reviewRequiredDetailsOut=No Details,bookingConfirmedOut=NO");
                }
         }
         catch (HttpException e) {
                        e.printStackTrace();
                        return null;
         }

        System.out.println("Completed " + task.getName()+ " "+task.getId()+  ": " + response);

        return task;

    }



    protected void run() {

        Task task = null;

        String response = null;

        init();

        try {


            response = client.startProcess("org.specialtripsagency:specialtripsagencyproject:2.0.0",
                    "org.specialtripsagency.specialtripsagencyprocess",
                    "applicantName="+rndChoice(NameChoice)+
                    ",emailAddress=email.com" +
                    ",numberOfTravelers="+rndChoice(NofTChoice)+
                    ",otherDetails=NO,fromDestination=London,toDestination=Edinburgh");

            System.out.println("Process started 1: " + response);

            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response.toString().getBytes()));
                System.out.println("Process  2: " + doc.getDoctype());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            XPath xpath = XPathFactory.newInstance().newXPath();


            try {
                processid = (String) xpath.evaluate("/process-instance/id", doc, STRING);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }


        } catch (HttpException e) {
            e.printStackTrace();
        }


        System.out.println("Process 3:" + processid); // Display the string.




        while (true) {

            task = executeNextTask (5);
            if (task == null)
                    break;
        }








    }
}
