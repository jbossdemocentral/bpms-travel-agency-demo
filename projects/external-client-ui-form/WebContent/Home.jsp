<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Booking Form</title>

<style>

body {
	font-family: sans-serif;
}

#header {
	background-color: black;
	color: white;
	text-align: center;
	padding: 5px;
}

#footer {
	text-align: center;
	padding: 1em;
}

form {
	border: 2px solid #f0f0f0;
}

form > div {
	padding: .5em;
}

form > div:nth-child(odd) {
	background-color: #f0f0f0;
}

form div label {
	display: inline-block;
	min-width: 25%;
}

form > div > div {
	display: inline-block;
}

input[type="submit"] {
	padding: .5em 1em .5em 1em;
	background-color: red;
	font-weight: bold;
	color: white;
	border: 0;
	margin: 1em;
	font-size: large;
}

h2 {
	font-size: large;
	color: red;
	margin: 0;
	padding: .4em;
	padding-top: 1.5em;
}

</style>

</head>
<body>

	<div id="header">
		<h1>SPECIAL TRIPS AGENCY - WEB FORM</h1>
	</div>

	<div id="section">
		<form action="SimpleServlet">
			<h2>Customer Details:</h2>
			
			<div>
				<label for = "applicantName">Applicant Name</label> 
				<div>
					<input type="text" id = "applicantName" name="applicantName" size="20px" />
				</div>
			</div>

			<div>
				<label for = "emailAddress">Email Address</label>
				<div>
					<input type="text" id = "emailAddress" name="emailAddress" size="20px" />
				</div>
			</div>

			<h2>Travel Details:</h2>
			<div>
				<label for = "numberOfTravellers">Number Of Travelers</label>
				<div>
					<input type="text" id = "numberOfTravellers" name="numberOfTravelers" size="20px" /> 
				</div>
			</div>
				
			<div>
				<label for = "fromDestination">From Destination</label>
				<div>
					<select id = "fromDestination" name="fromDestination">
						<option value="London">London</option>
					</select>
				</div>
			</div>
				
			<div>	
				<label for = "toDestination">To Destination </label>
				<div>
					<select id = "toDestination" name="toDestination">
						<option value="Edinburgh">Edinburgh</option>
					</select>
				</div>
			</div>

			<div>
				<label for = "preferredDateOfArrival">Preferred Date Of Arrival (YYYY-MM-DD)</label>
				<div>
					<input type="text" id = "preferredDateOfArrival" name="preferredDateOfArrival" size="20px" />
				</div>
			</div>
			
			<div> 
				<label for = "preferredDateOfDeparture">Preferred Date Of Departure (YYYY-MM-DD)</label>
			
				<div>	
					<input type="text" id = "preferredDateOfDeparture" name="preferredDateOfDeparture" size="20px" />
				</div>
			</div>

			<div>
				<label for = "otherDetails">Other Details</label>

				<div>
					<input type="text" id = "otherDetails" name="otherDetails" value="N/A" size="50px" />
				</div>
			</div>
		
			<input type="submit" value="SUBMIT"><br>
		</form>
	</div>

	<div id="footer">Powered by <strong>Red Hat JBoss BPM Suite</strong></div>

</body>
</html>
