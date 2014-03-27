#!/bin/bash

# The BASEURL in this script points to the Portal.
BASEURL=https://openmobile.ipass.com

Echo Enter the username 
read USER

Echo Enter the password for user $USER
Read -s PASSWORD

Echo Enter the Company ID
read COMPANY

# Login to the Portal as a user who has access to view their child companies' information
curl –k –c tmpCookies.txt "$BASEURL/moservices/rest/api/login?UserAgent=apiuser&username=$USER&password=$PASSWORD"

# Obtain the XML files containing the IDs and Names of all my child companies
curl –k –request GET –b tmpCookies.txt –o childcompanies.xml "$BASEURL/moservices/rest/api/ipass/$COMPANY/mo/omapi/findAllChildCompanies"

# Delete the cookies
rm tmpCookies.txt
