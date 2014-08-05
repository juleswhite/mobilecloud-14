## Warning

UNDER NO CIRCUMSTANCES SHOULD YOU USE THE INCLUDED KEYSTORE IN A PRODUCTION APP!!!
UNDER NO CIRCUMSTANCES SHOULD YOU USE THIS APP "AS IS" IN PRODUCTION!!!

## Running the Application

Please read the instructions carefully, the way that you run and access
the application has changed.

To run the application:

1. (Menu Bar) Run->Run Configurations
2. Under Java Applications, select your run configuration for this app
3. Open the Arguments tab
4. In VM Arguments, provide the following information to use the
   default keystore provided with the sample code:

   -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit

5. Note, this keystore is highly insecure! If you want more security, you 
   should obtain a real SSL certificate:

   http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
   
6. This keystore is not secured and should be in a more secure directory -- preferably
   completely outside of the app for non-test applications -- and with strict permissions
   on which user accounts can access it

## Accessing the Service

Note: you need to use "https" and port "8443":

https://localhost:8443/video

You will almost certainly see a warning about the site's certificate in your browser. This
warning is being generated because the keystore includes a certificate that has not been
signed by a certificate authority. 

## Overview

For detailed information on configuring a REAL certificate for an application
using Tomcat, please refer to:

http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html

UNDER NO CIRCUMSTANCES SHOULD YOU USE THE INCLUDED KEYSTORE IN A PRODUCTION APP!!!
UNDER NO CIRCUMSTANCES SHOULD YOU USE THIS APP AS IS IN PRODUCTION!!!

## What to Pay Attention to

In this version of the video service application, we have added https. See the Application
class for the changes.

