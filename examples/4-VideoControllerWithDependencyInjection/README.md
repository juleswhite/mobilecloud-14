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

In this version of the VideoSvc application, we have added dependency injection:

1. The "videos" member variable of VideoSvc is not explicitly initialized in the VideoSvc
   class. Instead, this member variable is marked with @Autowired. Spring automatically
   connects the value for this member variable to whatever type of VideoRepository we
   construct in our @Bean annotated Application.videoRepository() method. As long as
   your Application class has exactly one @Bean annotated method that returns an instance
   of a particular interface, Spring can automatically find every occurrence where you have
   asked for that interface to be @Autowired and inject the value you return from your
   method into those objects. 
2. This version updates the VideoSvcTest to show how mock objects can be injected into
   @Autowired member variables. In this case, a test mock VideoRepository (constructed
   using the Mockito framework) is injected into the "videos" member variable of VideoSvc.
3. The tests add a new integration test that fully "wires" your controller using dependency
   injection and sends mock HTTP requests to it. This test helps ensure that your Application
   is properly configuring all of the @Autowired values that are needed in your application
   and that they all work together correctly.
