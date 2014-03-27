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

# Login to the Portal as a user who has an account on OM Portal and has the privilege to use the API
curl –k –c tmpCookies.txt "$BASEURL/moservices/rest/api/login?UserAgent=apiuser&username=$USER&password=$PASSWORD"