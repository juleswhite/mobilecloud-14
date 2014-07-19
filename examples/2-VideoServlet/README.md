## Running the VideoServlet

To run the servlet:

Right-click on the build.gradle file in Eclipse, Gradle->Quick Tasks Launcher,
and then type "jettyRun" for the task. 

To stop the servlet:

Right-click on the build.gradle file in Eclipse, Gradle->Quick Tasks Launcher,
and then type "jettyStop" for the task.

## Accessing the Service

After launching the servlet, open your browser to the URL below to see a list of
the videos that have been added:

http://localhost:8080/2-VideoServlet/video

In order to add a video that you can see in the list, run the VideoServletHttpTest
JUnit test and then refresh your browser.


## Video Walkthrough

For a video walkthrough of the code, please see: 
https://class.coursera.org/mobilecloud-001/lecture/207

