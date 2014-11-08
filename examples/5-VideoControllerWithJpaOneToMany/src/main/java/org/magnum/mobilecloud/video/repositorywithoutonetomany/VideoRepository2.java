package org.magnum.mobilecloud.video.repositorywithoutonetomany;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store Video
 * objects and allow them to be searched by title.
 * 
 * @author jules
 *
 */
@Repository
public interface VideoRepository2 extends CrudRepository<Video2, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<Video2> findByName(String title);
	
	// Find all videos within a given category
	public Collection<Video2> findByCategory(String category);
}
