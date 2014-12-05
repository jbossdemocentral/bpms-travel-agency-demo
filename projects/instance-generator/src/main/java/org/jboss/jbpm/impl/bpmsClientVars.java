package org.jboss.jbpm.impl;

import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.lang.text.StrSubstitutor;
import org.json.simple.JSONArray;

/**
 * Created by aubbiali on 27/11/14.
 */
public class bpmsClientVars {
    public   String name;
    public   String type;
    public   Number min;
    public   Number max;
    public   ArrayList<String> choice;

    private String rndChoice(ArrayList<String> a) {
        int idx = new Random().nextInt(a.size());
        return a.get(idx);
    }

    private String rndInt (Number min, Number max) {
        Integer value = new Random().nextInt( max.intValue() - min.intValue());
        return value.toString() + "i";
    }



    public bpmsClientVars() {
        choice = new ArrayList<String>();
    }

    public String getValue() {
        if (type.equals("int")) {
            return rndInt(min,max);
        }
        else if (type.equals("choice")) {
            return rndChoice(choice);
        }
        else return null;

    }


}
