package org.magnum.mobilecloud.client.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.controller.Video;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

/**
 * 
 * This test sends a POST request to the VideoServlet to add a new video and
 * then sends a second GET request to check that the video showed up in the list
 * of videos.
 * 
 * The test requires that the application be running first (see the directions in
 * the README.md file for how to launch the application.
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that
 * just directly makes method calls on a VideoSvc object are essentially
 * identical. All that changes is the setup of the videoService variable.
 * Yes, this could be refactored to eliminate code duplication...but the
 * goal was to show how much Retrofit simplifies interaction with our 
 * service!
 * 
 * @author jules
 *
 */
public class VideoSvcClientApiTest {

	private final String TEST_URL = "http://localhost:8080";

	/**
	 * This is how we turn the VideoSvcApi into an object that
	 * will translate method calls on the VideoSvcApi's interface
	 * methods into HTTP requests on the server. Parameters / return
	 * values are being marshalled to/from JSON.
	 */
	private VideoSvcApi videoService = new RestAdapter.Builder()
			.setEndpoint(TEST_URL)
			.setLogLevel(LogLevel.FULL)
			.build()
			.create(VideoSvcApi.class);

	/**
	 * This test sends a POST request to the VideoServlet to add a new video and
	 * then sends a second GET request to check that the video showed up in the
	 * list of videos.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoAddAndList() throws Exception {
		// Information about the video
		String title = "Programming Cloud Services for Android Handheld Systems";
		String url = "http://coursera.org/some/video";
		long duration = 60 * 10 * 1000; // 10min in milliseconds
		Video video = new Video(title, url, duration);
		
		// Send the POST request to the VideoServlet using Retrofit to add the video.
		// Notice how Retrofit provides a nice strongly-typed interface to our
		// HTTP-accessible video service that is much cleaner than muddling around
		// with URL query parameters, etc.
		boolean ok = videoService.addVideo(video);
		assertTrue(ok);
		
		List<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
	}

}
