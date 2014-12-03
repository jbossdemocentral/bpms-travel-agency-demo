<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Booking Form</title>

<style>
#header {
	background-color: black;
	color: white;
	text-align: center;
	padding: 5px;
}

#footer {
	background-color: black;
	color: white;
	clear: both;
	text-align: center;
	padding: 5px;
}
</style>

</head>
<body>

	<div id="header">
		<h1>WELCOME TO SPECIAL TRIPS AGENCY WEB FORM!!!</h1>
	</div>

	<div id="section">
		<form action="SimpleServlet">
			<br>
			<h2>Customer Details:</h2>
			Applicant Name <input type="text" name="applicantName" size="20px" />
			<br> Email Address <input type="text" name="emailAddress"
				size="20px" /> <br>

			<h2>Travel Details:</h2>
			Number Of Travelers <input type="text" name="numberOfTravelers"
				size="20px" /> <br> From Destination <select
				name="fromDestination">
				<option value="London">London</option>
			</select><br> To Destination <select name="toDestination">
				<option value="Edinburgh">Edinburgh</option>
			</select><br> Preferred Date Of Arrival (YYYY-MM-DD)<input type="text"
				name="preferredDateOfArrival" size="20px" /> <br> Preferred
			Date Of Departure (YYYY-MM-DD)<input type="text"
				name="preferredDateOfDeparture" size="20px" /> <br> Other
			Details <input type="text" name="otherDetails" value="N/A"
				size="50px" /> <br> <br> <input type="submit"
				value="SUBMIT"><br>
		</form>
	</div>

	<div id="footer">Copyright © 2014 Special Trips Agency Inc.</div>

</body>
</html>
