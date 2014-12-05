package org.jboss.jbpm;

import org.jboss.jbpm.impl.bpmsClientConfig;

/**
 * Created by aubbiali on 05/11/14.
 */


public class bpmsClientMain {

       //  private static bpmsClientApp app;

 //       final static int MAX_INSTANCES=1;


        static bpmsClientConfig config;


        public static void main(String[] args) {

            String s=null;
            if (args.length> 0) s=args[0];

            config = new bpmsClientConfig();

            config.parseJSON(s);


            for (int i=0; i < config.MAX_INSTANCES.intValue(); i++) {
             bpmsClientThread3 t = new bpmsClientThread3("t"+i, config);

  //             bpmsClientThread t = new bpmsClientThread("t"+i);

            }




           // app = new bpmsClientApp();

           //  app.run();

        }


    }
