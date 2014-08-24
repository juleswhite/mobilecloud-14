## Warning

UNDER NO CIRCUMSTANCES SHOULD YOU USE THE INCLUDED KEYSTORE IN A PRODUCTION APP!!!
UNDER NO CIRCUMSTANCES SHOULD YOU USE THIS APP "AS IS" IN PRODUCTION!!!

## Running the Application

Please read the instructions carefully.

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

If you try to access the above URL in your browser, the server is going to generate an error 
that looks something like "An Authentication object was not found in the SecurityContext." 
If you want to use your browser to test the service, you will need to use a plug-in like 
Postman and an understanding of how to use it to manually construct and obtain a bearer token.

The VideoSvcClientApiTest shows how to programmatically access the video service. You should
look at the SecuredRestBuilder class that is used to automatically intercept requests to the
VideoSvcApi methods, automatically obtain an OAuth 2.0 bearer token if needed, and add this
bearer token to HTTP requests. 

## Overview

This example covers a very small piece of the OAuth 2.0 specification that is
relevant to mobile developers that would like to use their *own* service and
authenticate to that service using OAuth 2.0. The entire OAuth 2.0 specification
covers many more uses cases and is described in detail here:

http://oauth.net/2/

The two key classes to look at in this example are OAuth2SecurityConfiguration and
SecuredRestBuilder. These two classes handle the server-side of setting up and
enforcing OAuth 2.0 security and the client-side of authenticating with an
OAuth 2.0 protected service via a password grant.

For detailed information on configuring a REAL certificate for an application
using Tomcat, please refer to:

http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html

UNDER NO CIRCUMSTANCES SHOULD YOU USE THE INCLUDED KEYSTORE IN A PRODUCTION APP!!!

## What to Pay Attention to

In this version of the video service application, OAuth 2.0 has been used to protect
the various video service endpoints. Clients must request access through the /oauth/token
endpoint and then insert the token obtained into future requests for authorization.

The two key classes to look at in this example are OAuth2SecurityConfiguration and
SecuredRestBuilder. The VideoSvcClientApiTest shows how the client uses the SecuredRestBuilder.