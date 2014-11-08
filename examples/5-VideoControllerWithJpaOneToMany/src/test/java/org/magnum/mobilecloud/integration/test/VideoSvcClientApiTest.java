package org.magnum.mobilecloud.integration.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;
import org.magnum.mobilecloud.video.TestData;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.client.VideoSvcApi2;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.Category2;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.Video2;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

/**
 * 
 * This integration test sends a POST request to the VideoServlet to add a new video 
 * and then sends a second GET request to check that the video showed up in the list
 * of videos. Actual network communication using HTTP is performed with this test.
 * 
 * The test requires that the VideoSvc be running first (see the directions in
 * the README.md file for how to launch the Application).
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
public class VideoSvcClientApiTest {

	private final String TEST_URL_WITH_ONE_TO_MANY = "http://localhost:8080";
	
	private final String TEST_URL_WITHOUT_ONE_TO_MANY = "http://localhost:8080/2";

	private VideoSvcApi videoServiceWithOneToMany = new RestAdapter.Builder()
			.setEndpoint(TEST_URL_WITH_ONE_TO_MANY).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);
	
	private VideoSvcApi2 videoServiceWithoutOneToMany = new RestAdapter.Builder()
	.setEndpoint(TEST_URL_WITHOUT_ONE_TO_MANY).setLogLevel(LogLevel.FULL).build()
	.create(VideoSvcApi2.class);

	private Video video = TestData.randomVideo();
	
	private Video2 video2 = TestData.randomVideo2();
	
	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoAddAndListWithOneToMany() throws Exception {
		
		// If we try to add a Video to a Category that doesn't
		// exist yet, it should fail.
		
		try{
			// Add the video
			videoServiceWithOneToMany.addVideo(video);
			fail("It shouldn't be possible to add a Video to a Category that doens't exist");
		}catch(Exception e){
			// Good, the Video service prevented us from adding a Video to a Category
			// that doesn't exist
		}
		
		// Now, let's add the Video's Category to the server 
		boolean addedCategory = videoServiceWithOneToMany.addCategory(video.getCategory());
		assertTrue(addedCategory);
		
		// Once the target Video category exists, we can add the Video
		boolean addedVideo = videoServiceWithOneToMany.addVideo(video);
		assertTrue(addedVideo);

		// We should get back the video that we added above
		Collection<Video> videos = videoServiceWithOneToMany.getVideoList();
		assertTrue(videos.contains(video));
		
		// The Video should show up in the appropriate Category
		Collection<Video> videosByCategory = videoServiceWithOneToMany.getVideoListForCategory(video.getCategory().getName());
		assertTrue(videosByCategory.contains(video));
	}

	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoAddAndListWithoutOneToMany() throws Exception {
		
		// If we try to add a Video to a Category that doesn't
		// exist yet, it should fail.
		
		try{
			// Add the video
			videoServiceWithoutOneToMany.addVideo(video2);
			fail("It shouldn't be possible to add a Video to a Category that doens't exist");
		}catch(Exception e){
			// Good, the Video service prevented us from adding a Video to a Category
			// that doesn't exist
		}
		
		// Now, let's add the Video's Category to the server 
		Category2 category = new Category2();
		category.setName(video2.getCategory());
		boolean addedCategory = videoServiceWithoutOneToMany.addCategory(category);
		assertTrue(addedCategory);
		
		// Once the target Video category exists, we can add the Video
		boolean addedVideo = videoServiceWithoutOneToMany.addVideo(video2);
		assertTrue(addedVideo);

		// We should get back the video that we added above
		Collection<Video2> videos = videoServiceWithoutOneToMany.getVideoList();
		assertTrue(videos.contains(video2));
		
		// The Video should show up in the appropriate Category
		Collection<Video2> videosByCategory = videoServiceWithoutOneToMany.getVideoListForCategory(video2.getCategory());
		assertTrue(videosByCategory.contains(video2));
	}

	
}
