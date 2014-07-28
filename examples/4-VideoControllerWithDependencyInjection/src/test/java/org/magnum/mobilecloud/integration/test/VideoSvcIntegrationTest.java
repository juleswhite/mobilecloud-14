package org.magnum.mobilecloud.integration.test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.magnum.mobilecloud.video.TestData;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.controller.Application;
import org.magnum.mobilecloud.video.controller.Video;
import org.magnum.mobilecloud.video.controller.VideoSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * 
 * This test shows how to fully setup and configuration a controller (or you could
 * setup all controllers) for an integration test. The real Application class is used
 * to configure the application. Spring sets up almost all of the supporting infrastructure
 * that the Application has when you run it.
 * 
 * A MockMvc or "fake" web interface is create to send requests to your controller and
 * validate the responses. Unlike the unit test in VideoSvcTest, integration testing requires
 * sending mock HTTP requests to the controller and encoding the test data into JSON before
 * the mock HTTP request is sent.
 * 
 * There are a variety of annotations that are used to setup this integration test. These
 * annotations will primarily be the same across all of the integration tests for your
 * controllers. You can base other integration tests off the test below by simplying
 * changing the controller that is @Autowired and passed into MockMvcBuilder.standaloneSetup(..)
 * 
 * @author jules
 *
 */

// Tell Spring to setup a web container configuration for testing
@WebAppConfiguration
// Tell JUnit to run using Spring's special JUnit runner
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
// This is where you tell Spring the Application or Configuration object to use
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class VideoSvcIntegrationTest {

	// Ask Spring to automatically construct and inject your VideoSvc
	// into the test
	@Autowired
	private VideoSvc videoService;

	// This is the mock interface to our application that we will use to 
	// send mock HTTP requests
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		// Setup Spring test in standalone mode with our VideoSvc object
		// that it built
		mockMvc = MockMvcBuilders.standaloneSetup(videoService).build();
	}
	
	// This test is the integration testing equivalent of the
	// VideoSvcTest.testVideoAddAndList() test. The key difference is that
	// this integration testing version sets up the entire Spring infrastructure
	// supporting your application and configures your application using the
	// real objects that you specify in your Application class.
	@Test
	public void testVideoAddAndList() throws Exception {
		Video video = TestData.randomVideo();
		String videoJson = TestData.toJson(video);
		
		// Send a request that should invoke VideoSvc.addVideo(Video v)
		// and check that the request succeeded
		mockMvc.perform(
				post(VideoSvcApi.VIDEO_SVC_PATH)
				.contentType(MediaType.APPLICATION_JSON)
	            .content(videoJson))
	            .andExpect(status().isOk())
	            .andReturn();
		
		// Send a request that should invoke VideoSvc.getVideos()
		// and check that the Video object that we added above (as JSON)
		// is in the list of returned videos
		mockMvc.perform(
				get(VideoSvcApi.VIDEO_SVC_PATH))
	            .andExpect(status().isOk())
	            .andExpect(content().string(containsString(videoJson)))
	            .andReturn();
	}

}
