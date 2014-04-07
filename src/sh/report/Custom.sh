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
read MONTH

Echo Enter the filename including the extension, e.g. ClientUserVersionReport.html, or ClientUserDomainReport.html
read FILENAME

# Login to the Portal as a user who has access to view Custom Report
curl –k –c tmpCookies.txt "$BASEURL/moservices/rest/api/login?UserAgent=apiuser&username=$USER&password=$PASSWORD"

# Obtain a zip file containing the specific reports from all my child companies
curl –k –request GET –b tmpCookies.txt –o $filename.zip "$BASEURL/moservices/rest/api/ipass/$COMPANY/mo/reports/monthly/custom?User-Agent=apiuser&month=$MONTH&filename=$FILENAME"

# Delete the cookies
rm tmpCookies.txt