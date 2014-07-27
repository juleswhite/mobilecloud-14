package org.magnum.mobilecloud.video.controller;

import com.google.common.base.Objects;

/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * @author jules
 * 
 */
public class Video {

	private String name;
	private String url;
	private long duration;

	public Video(){}
	
	public Video(String name, String url, long duration) {
		this.name = name;
		this.url = url;
		this.duration = duration;
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

	/**
	 * Two Videos will generate the same hashcode if they have exactly
	 * the same values for their name, url, and duration.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing 
		return Objects.hashCode(name,url,duration);
	}

	/**
	 * Two Videos are considered equal if they have exactly
	 * the same values for their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Video){
			Video other = (Video)obj;
			// Google Guava provides great utilities for equals too! 
			return Objects.equal(name, other.name) 
					&& Objects.equal(url, other.url)
					&& duration == other.duration;
		}
		else {
			return false;
		}
	}

}
