## Running the Video Service Application

To run the application:

Right-click on the Application class in the org.magnum.mobilecloud.video
package, Run As->Java Application

To stop the application:

Open the Eclipse Debug Perspective (Window->Open Perspective->Debug), right-click on
the application in the "Debug" view (if it isn't open, Window->Show View->Debug) and
select Terminate

## Accessing the Service

To view a list of the videos that have been added, open your browser to the following
URL:

http://localhost:8080/video

To add a test video, run the VideoSvcClientApiTest by right-clicking on it in 
Eclipse->Run As->JUnit Test (make sure that you run the application first!)

## What to Pay Attention to

Notice how much has changed from the VideoServlet version of this cloud service:

1. The web.xml file has been eliminated and we just have Application.java now
2. Our VideoSvc has been dramatically simplified compared to the past VideoServlet and
   now relies on Spring to automatically marshall/unmarshall data sent to/from the client.
3. We have a type-safe interface for interacting with our VideoSvc from a client. This
  interface, when combined with Retrofit, dramatically simplifies client/server interactions.
4. Look at the two tests in src/test/java. The tests are vastly simpler than with the 
   VideoServlet version. In fact, the Retrofit version of the test (VideoSvcHttpTest) is
   essentially identical to the version that just constructs and tests a VideoSvc object.
   Using Retrofit, it looks like you are interacting with a local object -- even though 
   that object is actually sending HTTP requests to the server in response to your method
   calls.

