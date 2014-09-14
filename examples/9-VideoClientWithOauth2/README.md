## Warning

UNDER NO CIRCUMSTANCES SHOULD YOU USE THE INCLUDED NON-VALIDATION HTTP CLIENT IN A PRODUCTION APP!!!
UNDER NO CIRCUMSTANCES SHOULD YOU HARDCODE REAL AUTHENTICATION INFORMATION IN A PRODUCTION APP!!!

## Running the Application

Please read the instructions carefully.

If you are using Eclipse, you will need to manually enable annotation processing for ButterKnife
(a great library for Android!). To enable annotation processing, follow these instructions:

http://jakewharton.github.io/butterknife/ide-eclipse.html

If you are using Android Studio, you will need to create a build.gradle file with the following
libraries:

1. Retrofit
2. ButterKnife
3. Guava
4. Commons IO
5. GSON

To run the application:

1. Launch the application as an Android app
2. Launch the 9-VideoSeviceWithOAuth2 example and ensure that you
   can access https://10.0.2.2:8443 from the __browser__ of your Android 
   emulator (or https://<your_ip>:8443 on Genymotion)
3. In the VideoClient app, use the same server address and the user/pass
   that you configured
4. Run the VideoSvcClientApiTest to add a video to the server
5. Click login (if login does nothing, you may not have configured annotation
   processing for ButterKnife as described above).
6. The list of videos should be fetched from the server and displayed in the
   app
   
## OAuth 2.0 Authentication

The key logic for authenticating with the OAuth 2.0 endpoint in the Spring application
is encapsulated in SecuredRestBuilder and used by VideoSvc.   

Special thanks to Tomas Stubbs who posted a simplified version of the equivalent of the UnsafeHttpClient
for Android that consolidates the typical EasySSLSocketFactory and X509TrustManager
workarounds into a single file:

https://github.com/uscheller/EasyHttpClient

This version of the client is easier to use than the normal workarounds for self-signed
certificates on Android that require 3+ separate Java classes.
