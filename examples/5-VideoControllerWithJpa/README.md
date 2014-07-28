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

1. The VideoRepository interface defines the application's interface to the database.
   There is no implementation of the VideoRepository in the project, Spring dynamically
   creates the implementation when it discovers the @Repository annotated interface.
2. This "videos" member variable of the VideoSvc is automatically auto-wired with the
   implementation of the VideoRepository that Spring creates. 
3. The VideoRepository inherits methods, such as save(...), that are defined in the
   CrudRepository interface that it extends. 
4. The "compile("com.h2database:h2")" line in the build.gradle file adds the H2 database
   as a dependency and Spring Boot automatically discovers it and embeds a database 
   instance in the application. By default, the database is configured to be in-memory
   only and will not persist data across restarts. However, another database could
   easily be swapped in and data would be persisted durably.
5. Notice that the VideoRepository is automatically discovered by Spring.
