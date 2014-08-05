## Running the Video Service Application

To run the application:

Right-click on the Application class in the org.magnum.mobilecloud.video
package, Run As->Java Application

To stop the application:

Open the Eclipse Debug Perspective (Window->Open Perspective->Debug), right-click on
the application in the "Debug" view (if it isn't open, Window->Show View->Debug) and
select Terminate

## Accessing the Service

http://localhost:8080/video

To add a test video, run the VideoSvcClientApiTest by right-clicking on it in 
Eclipse->Run As->JUnit Test (make sure that you run the application first!)

## Overview

This version of the application further reduces the amount of code and greatly
enhances the available functions on the service. The video service in this version
supports sending HTTP requests to:

   1. List all videos by sending a GET request to /video 
   2. Add a video by sending a POST request to /video with the JSON for a video
   3. Get a specific video by sending a GET request to /video/{videoId}
      (e.g., /video/1 would return the JSON for the video with id=1)
   4. Send search requests to our findByXYZ methods to /video/search/findByXYZ
      (e.g., /video/search/findByName?title=Foo)

## What to Pay Attention to

In this version of the video service application, we have added Spring Data Rest,
which automatically provides all of the functions describe above for our repository.

1. This version completely eliminates the VideoSvc controller. Instead, we are using
   Spring Data Rest to magically create a controller that allows clients to save,
   findAll, and search for videos. The important configuration parameters to enable
   Spring Data Rest are in Application and VideoRepository. 
2. The Application class now extends RepositoryRestMvcConfiguration so that Spring Data
   Rest is automatically enabled.
3. The @RepositoryRestResource annotation on VideoRepository tells Spring Data Rest to
   expose the VideoRepository through a controller and map it to the 
   "/video" path.



