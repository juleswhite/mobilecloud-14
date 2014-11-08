package org.magnum.mobilecloud.video;

import java.util.UUID;

import org.magnum.mobilecloud.video.repository.Category;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.Video2;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class to aid in the construction of
 * Video objects with random names, urls, and durations.
 * The class also provides a facility to convert objects
 * into JSON using Jackson, which is the format that the
 * VideoSvc controller is going to expect data in for
 * integration testing.
 * 
 * @author jules
 *
 */
public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Construct and return a Video object with a
	 * rnadom name, url, and duration.
	 * 
	 * @return
	 */
	public static Video randomVideo() {
		// Information about the video
		// Construct a random identifier using Java's UUID class
		String id = UUID.randomUUID().toString();
		String title = "Video-"+id;
		String category = "Category-"+UUID.randomUUID().toString();
		Category cat = new Category();
		cat.setName(category);
		String url = "http://coursera.org/some/video-"+id;
		long duration = 60 * (int)Math.rint(Math.random() * 60) * 1000; // random time up to 1hr
		Video v = new Video(title, url, duration);
		v.setCategory(cat);
		return v;
	}
	
	/**
	 * Construct and return a Video object with a
	 * rnadom name, url, and duration.
	 * 
	 * @return
	 */
	public static Video2 randomVideo2() {
		// Information about the video
		// Construct a random identifier using Java's UUID class
		String id = UUID.randomUUID().toString();
		String title = "Video-"+id;
		String category = "Category-"+UUID.randomUUID().toString();
		String url = "http://coursera.org/some/video-"+id;
		long duration = 60 * (int)Math.rint(Math.random() * 60) * 1000; // random time up to 1hr
		Video2 v = new Video2(title, url, duration);
		v.setCategory(category);
		return v;
	}
	
	/**
	 *  Convert an object to JSON using Jackson's ObjectMapper
	 *  
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
