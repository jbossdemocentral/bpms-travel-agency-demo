JBoss BPM Suite Travel Agency Demo - COMPENSATION
==================================================
The online travel booking process has been extended to include compensation. But what is Compensation?

This is an online employee travel booking process project. It contains multimple web services for looking up data for the process
and rules to calculate pricing. Furthermore, there are several tasks that can be activated to evaluate pricing and to review the
final booking data before completing the booking.

Welcome to the JBoss BPM Travel Agency!


Compensation
-------------
Compensation is a means for “undoing” the effects of an action. For example, say you charge the amount of 10€ to a credit card. If you later detect that this was an error, you may want to undo this. One way of doing that is crediting the same account 10€.

This sounds like something that can be handled with transactions... only that there are a couple of problems:

    The service you are using might not support transactions.
    You might notice that it was wrong to charge the 10€ too late, at a time where the transaction is already committed.

In both cases we cannot simply “go back in time” and do as if nothing happened. On the contrary, we need to make something else happen, in order to compensate the effects of an action we erroneously committed.

How it works in our process
---------------------------
Compensation is set up in our process to "undo" or return any flight seats and hotel rooms that have been booked in the event of a failure to collect payment from the customer's payment card.
Therefore, the process starts with a booking of seats/rooms then proceeds to collect payment from the card... if the card is fraudulent (i.e. begins with 1234...) then process compensates the booked seats.

Starting the process and invoking compensation
------------------------------------------------
1. Start JBoss BPMS Server by running 'standalone.sh' or 'standalone.bat' in the <path-to-project>/target/jboss-eap-6.1/bin directory.
  
2. Start process with by entering the details below on the form on this link: http://localhost:8080/external-client-ui-form-1.0/

  ```
  Name: [your-name]

  Email Adress: [any-email] MUST BE VALID

  Number of Travellers: 2

  From Destination: London

  To Destination: Edinburgh

  Preferred Date of Departure: 2014-12-20

  Preferred Data of Arrival: 2014-12-29

  Other Details / Notes: [any-text]
  ```

3. Login to http://localhost:8080/business-central

  ```
  - login for admin and other roles (u:erics / p:bpmsuite1!)
  ```

4. Navigate to the "Tasks" tab and click on it. From the task in the list, click on the "Lock" icon to claim the task

5. Click on the "Work" tab from the resulting right-side pane window that opened and select the 2nd option checkbox (isBookingConfirmed).

6. Enter credit card details (beginning with 1234...) for compensation to be triggered., Expiry details of the card (e.g. 12/12) and your full name.

7. Check the logs and you will see that the process has been compensated.


8. To trigger different path for successful booking of Flights, just change the 'Credit Card details' to use any card number that does not begin with 1234....
