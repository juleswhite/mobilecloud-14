package org.magnum.mobilecloud.video.repository;

import com.google.common.base.Objects;

/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * You probably need to, at a minimum, add some annotations to this
 * class.
 * 
 * You are free to add annotations, members, and methods to this
 * class. However, you probably should not change the existing
 * methods or member variables. If you do change them, you need
 * to make sure that they are serialized into JSON in a way that
 * matches what is expected by the auto-grader.
 * 
 * @author mitchell
 */
public class Video {

	private long id;

	private String name;
	private String url;
	private long duration;
	private long likes;
	
	public Video() {
	}

	public Video(String name, String url, long duration, long likes) {
		super();
		this.name = name;
		this.url = url;
		this.duration = duration;
		this.likes = likes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLikes() {
		return likes;
	}
	
	public void setLikes(long likes) {
		this.likes = likes;
	}
	
	/**
	 * Two Videos will generate the same hashcode if they have exactly the same
	 * values for their name, url, and duration.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(name, url, duration);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Video) {
			Video other = (Video) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name)
					&& Objects.equal(url, other.url)
					&& duration == other.duration;
		} else {
			return false;
		}
	}

}
