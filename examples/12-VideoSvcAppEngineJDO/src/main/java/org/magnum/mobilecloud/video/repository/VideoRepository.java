package org.magnum.mobilecloud.video.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;

@Service
public class VideoRepository extends JDOCrudRepository<Video, Long>{

	public VideoRepository() {
		super(Video.class);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Video> findByName(String name){
		Query query = PMF.get().getPersistenceManager().newQuery(Video.class);
		query.setFilter("name == n");
		query.declareParameters("String n");
		return (List<Video>)query.execute(name);
	}

}
