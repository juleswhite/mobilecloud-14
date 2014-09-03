## Running the Video Service Application

To run the application:

1. Right-click on the Application class in the org.magnum.mobilecloud.video
package, Run As->Java Application and then stop the application after it launches
(this is to have Eclipse create a run configuraiton for you).
2. Run->Run Configurations
3. Under Java Applications, select your run configuration for this app
4. Open the Arguments tab
5. In VM Arguments, provide the following information for your Amazon AWS account:

   -DAWS_SECRET_KEY=your_secret_key -DAWS_ACCESS_KEY_ID=your_access_key

   You will need to obtain the secret key and access key from Amazon by signing up
   for an AWS account.

To stop the application:

Open the Eclipse Debug Perspective (Window->Open Perspective->Debug), right-click on
the application in the "Debug" view (if it isn't open, Window->Show View->Debug) and
select Terminate

## Deploying the Application to Amazon Elastic Beanstalk

Please see the course website for a step-by-step video in the Week 8 video
lectures.


## Accessing the Service

To view a list of the videos that have been added, open your browser to the following
URL:

http://localhost:8080/video

## What to Pay Attention to

In this version of the VideoSvc application, we are using DynamoDB to store data.
See the Video class for the annotation changes that this requires. See the Application
class for the configuration of your Amazon AWS credentials. An ApplicationServletInitializer
class has been added to boostrap the application when it is deployed to a stand-alone
Tomact instance in Amazon Elastic Beanstalk.

