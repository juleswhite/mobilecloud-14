package org.magnum.mobilecloud.video.repository;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents a Category of a Video. Each Video can
 * be in exactly one Category (e.g., romance, action, horror, etc.).
 * This version of the class uses @OneToMany.
 * 
 * @author jules
 *
 */
@Entity
public class Category {

	@Id
	private String name;

	// We ask Jackson to ignore this property when serializing
	// a Category to avoid the problem of circular references
	// in our JSON. Since this is a bi-directional relationship, 
	// if we didn't ignore this property, Jackson would generate
	// a stack overflow by trying to serialize the Videos in the
	// category, which in turn refer back to the category, which
	// would refer back to the videos, etc. creating an infinite
	// loop. By ignoring this property, we break the chain.
	//
	// If you use Spring Data REST, it handles this for you 
	// automatically and inserts links into the generated JSON
	// to reference the associated objects.
	@JsonIgnore
	// We add a OneToMany annotation indicating that each Category (e.g., "the one")
	// can refer to multiple videos (e.g., "the many"). The "mappedBy"
	// attribute tells JPA the name of the property on the Video object
	// that refers to a category (e.g., Video.category). In this case,
	// the relationship is bi-directional since both the Video and the
	// Category know about each other. It is also possible for only one
	// class to be aware of the other.
	//
	// You do not have to create the table for this class before using @OneToMany.
	// The needed tables will automatically be created for you.
	@OneToMany(mappedBy="category")
	private Collection<Video> videos;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * JPA will automatically retrieve all Videos that have a category
	 * that refers to the primary key of current category when you call
	 * this method. Since each category has a unique name, if you assign
	 * a video to a given category name, then calling getVideos() on the
	 * Category object that has that name will include that video. JPA
	 * automatically updates this list as Video objects are added that
	 * refer to existing Categories. There is no need to explicitly tell
	 * JPA to update the list of videos for a given Category when you 
	 * save a new Video.
	 * 
	 */
	public Collection<Video> getVideos() {
		return videos;
	}

	public void setVideos(Collection<Video> videos) {
		this.videos = videos;
	}

}
