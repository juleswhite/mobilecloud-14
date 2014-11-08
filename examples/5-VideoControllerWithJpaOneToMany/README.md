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

In this version of the VideoSvc application, we are using @OneToMany to create a relationship
between Video and Category objects. A Video can belong to exactly one Category. A Category
can have multiple Videos associated with it. The example also provides sample code for 
implementing the same functionality without @OneToMany. All of the classes marked "2" are the
versions of the implementation that do not use @OneToMany. 
