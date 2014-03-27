#!/bin/bash

# The BASEURL in this script points to the Portal.
BASEURL=https://openmobile.ipass.com

Echo Enter the username 
read USER

Echo Enter the password for user $USER
Read -s PASSWORD

Echo Enter the Company ID
read COMPANY

Echo Enter the MONTH in the format YYYY-MM
read MONTH

# Login to the Portal as a user who has access to download Monthly CDRs
curl –k –c tmpCookies.txt "$BASEURL/moservices/rest/api/login?UserAgent=apiuser&username=$USER&password=$PASSWORD"

# Download the Monthly CDR file and save it as monthly_cdr.csv locally
curl –k –request GET –b tmpCookies.txt –o monthly_cdr.csv "$BASEURL/moservices/rest/api/ipass/$COMPANY/mo/cdrReports/monthly/cdr?User-Agent=apiuser&month=$MONTH"

# Delete the cookies
rm tmpCookies.txt