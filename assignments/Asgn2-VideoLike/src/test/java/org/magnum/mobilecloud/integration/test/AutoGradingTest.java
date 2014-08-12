package org.magnum.mobilecloud.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.magnum.autograder.junit.Rubric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.magnum.mobilecloud.video.TestData;
import org.magnum.mobilecloud.video.client.SecuredRestBuilder;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

/**
 * A test for the Asgn2 video service
 * 
 * @author mitchell
 */
public class AutoGradingTest {

	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}

	private final String TEST_URL = "https://localhost:8443";

	private final String USERNAME1 = "admin";
	private final String USERNAME2 = "user0";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";

	private VideoSvcApi readWriteVideoSvcUser1 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
			// .setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME1).setPassword(PASSWORD).setClientId(CLIENT_ID)
			.build().create(VideoSvcApi.class);

	private VideoSvcApi readWriteVideoSvcUser2 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
			// .setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME2).setPassword(PASSWORD).setClientId(CLIENT_ID)
			.build().create(VideoSvcApi.class);

	private Video video = TestData.randomVideo();


	@Rubric(value = "Video data is preserved", 
			goal = "The goal of this evaluation is to ensure that your Spring controller(s) "
			+ "properly unmarshall Video objects from the data that is sent to them "
			+ "and that the HTTP API for adding videos is implemented properly. The"
			+ " test checks that your code properly accepts a request body with"
			+ " application/json data and preserves all the properties that are set"
			+ " by the client. The test also checks that you generate an ID and data"
			+ " URL for the uploaded video.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/61 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testAddVideoMetadata() throws Exception {
		Video received = readWriteVideoSvcUser1.addVideo(video);
		assertEquals(video.getName(), received.getName());
		assertEquals(video.getDuration(), received.getDuration());
		assertTrue(received.getLikes() == 0);
		assertTrue(received.getId() > 0);
	}

	@Rubric(value = "The list of videos is updated after an add", 
			goal = "The goal of this evaluation is to ensure that your Spring controller(s) "
			+ "can add videos to the list that is stored in memory on the server."
			+ " The test also ensure that you properly return a list of videos"
			+ " as JSON.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/61 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testAddGetVideo() throws Exception {
		readWriteVideoSvcUser1.addVideo(video);
		Collection<Video> stored = readWriteVideoSvcUser1.getVideoList();
		assertTrue(stored.contains(video));
	}

	@Rubric(value = "Requests without authentication token are denied.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "properly authenticates queries using the OAuth Password Grant flow."
			+ "Any query that does not contain the correct authorization token"
			+ "should be denied with a 401 error.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/117 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/127 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/123 ")
	@Test
	public void testDenyVideoAddWithoutOAuth() throws Exception {
		ErrorRecorder error = new ErrorRecorder();

		// Create an insecure version of our Rest Adapter that doesn't know how
		// to use OAuth.
		VideoSvcApi insecurevideoService = new RestAdapter.Builder()
				.setClient(
						new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
				.setErrorHandler(error).build().create(VideoSvcApi.class);
		try {
			// This should fail because we haven't logged in!
			insecurevideoService.addVideo(video);

			fail("Yikes, the security setup is horribly broken and didn't require the user to authenticate!!");

		} catch (Exception e) {
			// Ok, our security may have worked, ensure that
			// we got a 401
			assertEquals(HttpStatus.SC_UNAUTHORIZED, error.getError()
					.getResponse().getStatus());
		}

		// We should NOT get back the video that we added above!
		Collection<Video> videos = readWriteVideoSvcUser1.getVideoList();
		assertFalse(videos.contains(video));
	}

	@Rubric(value = "A user can like/unlike a video and increment/decrement the like count", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to like/unlike videos using the /video/{id}/like endpoint, and"
			+ "and the /video/{id}/unlike endpoint."
			+ "Once a user likes/unlikes a video, the count of users that like that video"
			+ "should be incremented/decremented.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/121 ")
	@Test
	public void testLikeCount() throws Exception {

		// Add the video
		Video v = readWriteVideoSvcUser1.addVideo(video);

		// Like the video
		readWriteVideoSvcUser1.likeVideo(v.getId());

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is 1
		assertTrue(v.getLikes() == 1);

		// Unlike the video
		readWriteVideoSvcUser1.unlikeVideo(v.getId());

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is 0
		assertTrue(v.getLikes() == 0);
	}

	@Rubric(value = "A user can like/unlike a video and be added to/removed from the \"liked by\" list.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to like/unlike videos using the /video/{id}/like endpoint"
			+ "and the /video/{id}/unlike endpoint."
			+ "Once a user likes/unlikes a video, the username should be added to/removed from the "
			+ "list of users that like that video.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/121 ")
	@Test
	public void testLikedBy() throws Exception {

		// Add the video
		Video v = readWriteVideoSvcUser1.addVideo(video);

		// Like the video
		readWriteVideoSvcUser1.likeVideo(v.getId());

		Collection<String> likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());

		// Make sure we're on the list of people that like this video
		assertTrue(likedby.contains(USERNAME1));
		
		// Have the second user like the video
		readWriteVideoSvcUser2.likeVideo(v.getId());
		
		// Make sure both users show up in the like list
		likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());
		assertTrue(likedby.contains(USERNAME1));
		assertTrue(likedby.contains(USERNAME2));

		// Unlike the video
		readWriteVideoSvcUser1.unlikeVideo(v.getId());

		// Get the video again
		likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());

		// Make sure user1 is not on the list of people that liked this video
		assertTrue(!likedby.contains(USERNAME1));
		
		// Make sure that user 2 is still there
		assertTrue(likedby.contains(USERNAME2));
	}

	@Rubric(value = "A user is only allowed to like a video once.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "restricts users to liking a video only once. "
			+ "This test simply attempts to like a video twice and then checks that "
			+ "the like count is only 1.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/121"
	)
	@Test
	public void testLikingTwice() throws Exception {

		// Add the video
		Video v = readWriteVideoSvcUser1.addVideo(video);

		// Like the video
		readWriteVideoSvcUser1.likeVideo(v.getId());

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is 1
		assertTrue(v.getLikes() == 1);

		try {
			// Like the video again.
			readWriteVideoSvcUser1.likeVideo(v.getId());

			fail("The server let us like a video twice without returning a 400");
		} catch (RetrofitError e) {
			// Make sure we got a 400 Bad Request
			assertEquals(400, e.getResponse().getStatus());
		}

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is still 1
		assertTrue(v.getLikes() == 1);
	}

	@Rubric(value = "A user cannot like a non-existant video", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "won't crash if a user attempts to like a non-existant video. "
			+ "This test simply attempts to like a non-existant video then checks "
			+ "that a 404 Not Found response is returned.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/121"
	)
	@Test
	public void testLikingNonExistantVideo() throws Exception {

		try {
			// Like the video again.
			readWriteVideoSvcUser1.likeVideo(getInvalidVideoId());

			fail("The server let us like a video that doesn't exist without returning a 404.");
		} catch (RetrofitError e) {
			// Make sure we got a 400 Bad Request
			assertEquals(404, e.getResponse().getStatus());
		}
	}

	@Rubric(value = "A user can find a video by providing its name", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to find videos by searching for the video's name.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testFindByName() {

		// Create the names unique for testing.
		String[] names = new String[3];
		names[0] = "The Cat";
		names[1] = "The Spoon";
		names[2] = "The Plate";

		// Create three random videos, but use the unique names
		ArrayList<Video> videos = new ArrayList<Video>();

		for (int i = 0; i < names.length; ++i) {
			videos.add(TestData.randomVideo());
			videos.get(i).setName(names[i]);
		}

		// Add all the videos to the server
		for (Video v : videos){
			readWriteVideoSvcUser1.addVideo(v);
		}

		// Search for "The Cat"
		Collection<Video> searchResults = readWriteVideoSvcUser1.findByTitle(names[0]);
		assertTrue(searchResults.size() > 0);

		// Make sure all the returned videos have "The Cat" for their title
		for (Video v : searchResults) {
			assertTrue(v.getName().equals(names[0]));
		}
	}

	/**
	 * Test finding a video by its duration.
	 */
	@Rubric(value = "A user can find videos that have a duration less than a certain value.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to find videos by searching for videos with a duration "
			+ "less that a specified value.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testFindByDurationLessThan() {

		// Create the durations unique for testing.
		long[] durations = new long[3];
		durations[0] = 1;
		durations[1] = 5;
		durations[2] = 9;

		// Create three random videos, but use the unique durations
		ArrayList<Video> videos = new ArrayList<Video>();

		for (int i = 0; i < durations.length; ++i) {
			videos.add(TestData.randomVideo());
			videos.get(i).setDuration(durations[0]);
		}

		// Add all the videos to the server
		for (Video v : videos){
			readWriteVideoSvcUser1.addVideo(v);
		}

		// Search for "The Cat"
		Collection<Video> searchResults = readWriteVideoSvcUser1.findByDurationLessThan(6L);
		// Make sure that we have at least two videos
		assertTrue(searchResults.size() > 1);

		for (Video v : searchResults) {
			// Make sure that all of the videos are of the right duration
			assertTrue(v.getDuration() < 6);
		}
	}

	private long getInvalidVideoId() {
		Set<Long> ids = new HashSet<Long>();
		Collection<Video> stored = readWriteVideoSvcUser1.getVideoList();
		for (Video v : stored) {
			ids.add(v.getId());
		}

		long nonExistantId = Long.MIN_VALUE;
		while (ids.contains(nonExistantId)) {
			nonExistantId++;
		}
		return nonExistantId;
	}

}
