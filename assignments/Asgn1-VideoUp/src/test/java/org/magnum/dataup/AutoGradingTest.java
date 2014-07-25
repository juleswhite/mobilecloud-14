/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

/**
 *                       DO NOT MODIFY THIS CLASS
 *                       
                    ___                    ___           ___                            
     _____         /\  \                  /\  \         /\  \                           
    /::\  \       /::\  \                 \:\  \       /::\  \         ___              
   /:/\:\  \     /:/\:\  \                 \:\  \     /:/\:\  \       /\__\             
  /:/  \:\__\   /:/  \:\  \            _____\:\  \   /:/  \:\  \     /:/  /             
 /:/__/ \:|__| /:/__/ \:\__\          /::::::::\__\ /:/__/ \:\__\   /:/__/              
 \:\  \ /:/  / \:\  \ /:/  /          \:\~~\~~\/__/ \:\  \ /:/  /  /::\  \              
  \:\  /:/  /   \:\  /:/  /            \:\  \        \:\  /:/  /  /:/\:\  \             
   \:\/:/  /     \:\/:/  /              \:\  \        \:\/:/  /   \/__\:\  \            
    \::/  /       \::/  /                \:\__\        \::/  /         \:\__\           
     \/__/         \/__/                  \/__/         \/__/           \/__/           
      ___           ___                                     ___                         
     /\  \         /\  \         _____                     /\__\                        
    |::\  \       /::\  \       /::\  \       ___         /:/ _/_         ___           
    |:|:\  \     /:/\:\  \     /:/\:\  \     /\__\       /:/ /\__\       /|  |          
  __|:|\:\  \   /:/  \:\  \   /:/  \:\__\   /:/__/      /:/ /:/  /      |:|  |          
 /::::|_\:\__\ /:/__/ \:\__\ /:/__/ \:|__| /::\  \     /:/_/:/  /       |:|  |          
 \:\~~\  \/__/ \:\  \ /:/  / \:\  \ /:/  / \/\:\  \__  \:\/:/  /      __|:|__|          
  \:\  \        \:\  /:/  /   \:\  /:/  /   ~~\:\/\__\  \::/__/      /::::\  \          
   \:\  \        \:\/:/  /     \:\/:/  /       \::/  /   \:\  \      ~~~~\:\  \         
    \:\__\        \::/  /       \::/  /        /:/  /     \:\__\          \:\__\        
     \/__/         \/__/         \/__/         \/__/       \/__/           \/__/        
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.magnum.autograder.junit.Rubric;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class AutoGradingTest {

	private static final String SERVER = "http://localhost:8080";

	private File testVideoData = new File(
			"src/test/resources/test.mp4");
	
	private Video video = Video.create().withContentType("video/mp4")
			.withDuration(123).withSubject(UUID.randomUUID().toString())
			.withTitle(UUID.randomUUID().toString()).build();

	private VideoSvcApi videoSvc = new RestAdapter.Builder()
			.setEndpoint(SERVER).build()
			.create(VideoSvcApi.class);

	@Rubric(
			value="Video data is preserved",
			goal="The goal of this evaluation is to ensure that your Spring controller(s) "
					+ "properly unmarshall Video objects from the data that is sent to them "
					+ "and that the HTTP API for adding videos is implemented properly. The"
					+ " test checks that your code properly accepts a request body with"
					+ " application/json data and preserves all the properties that are set"
					+ " by the client. The test also checks that you generate an ID and data"
					+ " URL for the uploaded video.",
			points=20.0,
			reference="This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/61 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/67 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/71"
			)
	@Test
	public void testAddVideoMetadata() throws Exception {
		Video received = videoSvc.addVideo(video);
		assertEquals(video.getTitle(), received.getTitle());
		assertEquals(video.getDuration(), received.getDuration());
		assertEquals(video.getContentType(), received.getContentType());
		assertEquals(video.getLocation(), received.getLocation());
		assertEquals(video.getSubject(), received.getSubject());
		assertTrue(received.getId() > 0);
		assertTrue(received.getDataUrl() != null);
	}
	
	@Rubric(
			value="The list of videos is updated after an add",
			goal="The goal of this evaluation is to ensure that your Spring controller(s) "
					+ "can add videos to the list that is stored in memory on the server."
					+ " The test also ensure that you properly return a list of videos"
					+ " as JSON.",
			points=20.0,
			reference="This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/61 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/67 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/71"
			)
	@Test
	public void testAddGetVideo() throws Exception {
		videoSvc.addVideo(video);
		Collection<Video> stored = videoSvc.getVideoList();
		assertTrue(stored.contains(video));
	}
	
	@Rubric(
			value="Mpeg video data can be submitted for a video",
			goal="The goal of this evaluation is to ensure that your Spring controller(s) "
					+ "allow mpeg video data to be submitted for a video. The test also"
					+ " checks that the controller(s) can serve that video data to the"
					+ " client.",
			points=20.0,
			reference="This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/69 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/65 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/71 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/207"
			)
	@Test
	public void testAddVideoData() throws Exception {
		Video received = videoSvc.addVideo(video);
		VideoStatus status = videoSvc.setVideoData(received.getId(),
				new TypedFile(received.getContentType(), testVideoData));
		assertEquals(VideoState.READY, status.getState());
		
		Response response = videoSvc.getData(received.getId());
		assertEquals(200, response.getStatus());
		
		InputStream videoData = response.getBody().in();
		byte[] originalFile = IOUtils.toByteArray(new FileInputStream(testVideoData));
		byte[] retrievedFile = IOUtils.toByteArray(videoData);
		assertTrue(Arrays.equals(originalFile, retrievedFile));
	}
	
	@Rubric(
			value="Requests for non-existant video data return a 404",
			goal="The goal of this evaluation is to ensure that your Spring controller(s) "
					+ "properly indicate to the client with a 404 response when the client"
					+ " sends a request for video data for a video that does not have any"
					+ " video data.",
			points=10.0,
			reference="This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/65 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/207"
			)
	@Test
	public void testGetNonExistantVideosData() throws Exception {
		
		long nonExistantId = getInvalidVideoId();
		
		try{
			Response r = videoSvc.getData(nonExistantId);
			assertEquals(404, r.getStatus());
		}catch(RetrofitError e){
			assertEquals(404, e.getResponse().getStatus());
		}
	}
	
	@Rubric(
			value="Attempting to submit video data for a non-existant video generates a 404",
			goal="The goal of this evaluation is to ensure that your Spring controller(s) "
					+ "produce a 404 error if a client attempts to submit video data for"
					+ " a video that does not exist.",
			points=10.0,
			reference="This test is derived from the material in these videos: "
			        + "https://class.coursera.org/mobilecloud-001/lecture/207 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/69 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/65"
			)
	@Test
	public void testAddNonExistantVideosData() throws Exception {
		long nonExistantId = getInvalidVideoId();
		try{
			videoSvc.setVideoData(nonExistantId, new TypedFile(video.getContentType(), testVideoData));
			fail("The client should receive a 404 error code and throw an exception if an invalid"
					+ " video ID is provided in setVideoData()");
		}catch(RetrofitError e){
			assertEquals(404, e.getResponse().getStatus());
		}
	}

	private long getInvalidVideoId() {
		Set<Long> ids = new HashSet<Long>();
		Collection<Video> stored = videoSvc.getVideoList();
		for(Video v : stored){
			ids.add(v.getId());
		}
		
		long nonExistantId = Long.MIN_VALUE;
		while(ids.contains(nonExistantId)){
			nonExistantId++;
		}
		return nonExistantId;
	}

}
