package org.magnum.mobilecloud.video.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.magnum.mobilecloud.video.controller.Video;

/**
 * An implementation of the VideoRepository that allows duplicate
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
public class AllowsDuplicatesVideoRepository implements VideoRepository {

	// Lists allow duplicate objects that are .equals() to
	// each other
	//
	// Assume a lot more reads than writes
	private List<Video> videoList = new CopyOnWriteArrayList<Video>();
	
	@Override
	public boolean addVideo(Video v) {
		return videoList.add(v);
	}

	@Override
	public Collection<Video> getVideos() {
		return videoList;
	}

	// Search the list of videos for ones with
	// matching titles.
	@Override
	public Collection<Video> findByTitle(String title) {
		Set<Video> matches = new HashSet<>();
		for(Video video : videoList){
			if(video.getName().equals(title)){
				matches.add(video);
			}
		}
		return matches;
	}

}
