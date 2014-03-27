#!/bin/bash

# The BASEURL in this script points to the Portal.
BASEURL=https://openmobile.ipass.com

Echo Enter the username 
read USER

Echo Enter the password for user $USER
Read -s PASSWORD

Echo Enter the Company ID
read COMPANY

Echo Enter the month in the format YYYY-MM
read month

# Login to the Portal as a user who has access to view the Network & Client User Report
curl –k –c tmpCookies.txt "$BASEURL/moservices/rest/api/login?UserAgent=apiuser&username=$USER&password=$PASSWORD"

# Obtain the XML file containing all the records as well as the count summary
curl –k –request GET –b tmpCookies.txt –o networkAndClientUsers.xml "$BASEURL/moservices/rest/api/ipass/$COMPANY/mo/cdrReports/networkAndClientUser?User-Agent=apiuser&month=$MONTH"

# Obtain the CSV file containing all the Network & Client Users records
curl –k –request GET –b tmpCookies.txt –o networkAndClientUsers.csv "$BASEURL/moservices/rest/api/ipass/$COMPANY/mo/cdrReports/monthly/networkuser?User-Agent=apiuser&month=$MONTH"

# Delete the cookies
rm tmpCookies.txt