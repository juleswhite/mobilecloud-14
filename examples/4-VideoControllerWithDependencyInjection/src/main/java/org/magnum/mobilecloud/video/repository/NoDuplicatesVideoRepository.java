package org.magnum.mobilecloud.video.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.magnum.mobilecloud.video.controller.Video;

/**
 * An implementation of the VideoRepository that does not allow duplicate
 * Videos. 
 * 
 * Yes...there is a lot of code duplication with NoDuplicatesVideoRepository
 * that could be refactored into a base class or helper object. The
 * goal was to have as few classes as possible in the example and so
 * we did not do that refactoring.
 * 
 * @author jules
 *
 */
public class NoDuplicatesVideoRepository implements VideoRepository {

	// Sets only store one instance of each object and will not
	// store a duplicate instance if two objects are .equals()
	// to each other.
	//
	private Set<Video> videoSet = Collections.newSetFromMap(
	        new ConcurrentHashMap<Video, Boolean>());
	
	@Override
	public boolean addVideo(Video v) {
		return videoSet.add(v);
	}

	@Override
	public Collection<Video> getVideos() {
		return videoSet;
	}

	// Search the list of videos for ones with
	// matching titles.
	@Override
	public Collection<Video> findByTitle(String title) {
		Set<Video> matches = new HashSet<>();
		for(Video video : videoSet){
			if(video.getName().equals(title)){
				matches.add(video);
			}
		}
		return matches;
	}

}
