package org.magnum.mobilecloud.controller.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.magnum.mobilecloud.video.TestData;
import org.magnum.mobilecloud.video.controller.Video;
import org.magnum.mobilecloud.video.controller.VideoSvc;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * 
 * This test directly invokes the methods on VideoSvc to test them. The test
 * uses the Mockito library to inject a mock VideoRepository through dependency
 * injection into the VideoSvc object.
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just
 * directly makes method calls on a VideoSvc object are essentially identical.
 * All that changes is the setup of the videoService variable. Yes, this could
 * be refactored to eliminate code duplication...but the goal was to show how
 * much Retrofit simplifies interaction with our service!
 * 
 * @author jules
 *
 */
public class VideoSvcTest {

	// This tells Mockito to create a mock object for the VideoRepository
	// implementation that will be used for this test. A mock object is a
	// "fake" implementation of the interface that we can script to provide
	// specific outputs in response to different inputs.
	@Mock
	private VideoRepository videoRepository;

	// Automatically inject the mock VideoRepository into the VideoSvc
	// object
	@InjectMocks
	private VideoSvc videoService;

	private Video video = TestData.randomVideo();

	@Before
	public void setUp() {
		// Process mock annotations and inject the mock VideoRepository
		// into the VideoSvc object
		MockitoAnnotations.initMocks(this);

		// Tell the mock VideoRepository implementation to always return
		// true when its addVideo() method is called
		when(videoRepository.addVideo(any(Video.class))).thenReturn(true);
		
		// Tell the mock VideoRepository to always return the random Video
		// object that we create above when its getVideos() method is called
		when(videoRepository.getVideos()).thenReturn(Arrays.asList(video));
	}
	
	
	// Yes, this test doesn't do much because VideoSvc is
	// essentially delegating to VideoRepository. The goal is to
	// provide a simple example of testing controllers with mock
	// objects and dependency injection.
	@Test
	public void testVideoAddAndList() throws Exception {

		// Ensure that calling addVideo works
		boolean ok = videoService.addVideo(video);
		assertTrue(ok);

		// Make sure that the Video we added is in the list
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
	}

}
