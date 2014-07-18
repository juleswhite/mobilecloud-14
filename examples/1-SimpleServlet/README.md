## Running the EchoServlet ##

To run the servlet:

Right-click on the build.gradle file in Eclipse, Gradle->Quick Tasks Launcher,
and then type "jettyRun" for the task. 

To stop the servlet:

Right-click on the build.gradle file in Eclipse, Gradle->Quick Tasks Launcher,
and then type "jettyStop" for the task. 

## Accessing the Service

After launching the EchoServlet, you can interact with it by opening a web
browser to:

http://localhost:8080/1-SimpleServlet/echo?msg=1234test

Changing the "msg" URL query parameter will allow you to send different values
to echo to the EchoServlet.

## What to Pay Attention to

1. Take a look at the web.xml file in src/main/webapp/WEB-INF/web.xml and
   see how the routing of requests to the EchoServlet was setup
2. Notice how the EchoServlet extracts the "msg" parameter from the request
3. Notice that the EchoServlet explicitly sets a content-type for the response
   so that the client will know how to interpret the data in the body of
   the response. If you change this content-type, it will affect how your
   browser displays the result.
4. Look at the EchoServletHttpTest for an example of how to programmatically
   send an HTTP GET request to the servlet.

## Security Considerations

Although this servlet doesn't store any client-provided data, it could 
potentially be the victim of an injection attack. 

See this video for a discussion of the injection attack that could 
potentially be used against the EchoSevlet: 

https://class.coursera.org/mobilecloud-001/lecture/209