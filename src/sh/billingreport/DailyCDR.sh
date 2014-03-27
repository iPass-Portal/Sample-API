#!/bin/bash

# The BASEURL in this script points to the Portal.
BASEURL=https://openmobile.ipass.com

Echo Enter the username 
read USER

Echo Enter the password for user $USER
Read -s PASSWORD

Echo Enter the Company ID
read COMPANY

Echo Enter the date in the format YYYY-MM-DD
read DATE

# Login to the Portal as a user who has access to download Daily CDRs
curl –k –c tmpCookies.txt "$BASEURL/moservices/rest/api/login?UserAgent=apiuser&username=$USER&password=$PASSWORD"

# Download the Daily CDR file and save it as daily_cdr.csv locally
curl –k –request GET –b tmpCookies.txt –o daily_cdr.csv "$BASEURL/moservices/rest/api/ipass/$COMPANY/mo/cdrReports/daily/cdrdaily?User-Agent=apiuser&date=$DATE"

# Delete the cookies
rm tmpCookies.txt