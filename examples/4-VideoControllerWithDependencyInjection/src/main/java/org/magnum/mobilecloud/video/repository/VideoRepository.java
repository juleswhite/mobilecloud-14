package org.magnum.mobilecloud.video.repository;

import java.util.Collection;

import org.magnum.mobilecloud.video.controller.Video;

/**
 * An interface for a repository that can store Video
 * objects and allow them to be searched by title.
 * 
 * @author jules
 *
 */
public interface VideoRepository {

	// Add a video
	public boolean addVideo(Video v);
	
	// Get the videos that have been added so far
	public Collection<Video> getVideos();
	
	// Find all videos with a matching title (e.g., Video.name)
	public Collection<Video> findByTitle(String title);
	
}
