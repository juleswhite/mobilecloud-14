package org.magnum.mobilecloud.video.repositorywithoutonetomany;

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
public interface CategoryRepository2 extends CrudRepository<Category2, String>{

	
}
