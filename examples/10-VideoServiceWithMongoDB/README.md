## Running the Video Service Application

__Read this First__
To run this application, you will need to download, install, and launch MongoDB
on your local machine: http://www.mongodb.org/

To run the application:

1. Right-click on the Application class in the org.magnum.mobilecloud.video
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

In this version of the VideoSvc application, we are using MongoDB to store data.
See the Video and VideoRepository classes for the annotation changes that this 
requires. See the src/main/resources/application.properties file for configuration
options if you want to connect to a remote MongoDB instance.

