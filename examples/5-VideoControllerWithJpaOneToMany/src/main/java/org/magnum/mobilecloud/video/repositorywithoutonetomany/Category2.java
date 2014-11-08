package org.magnum.mobilecloud.video.repositorywithoutonetomany;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This class represents a Category of a Video. Each Video can
 * be in exactly one Category (e.g., romance, action, horror, etc.).
 * 
 * This version of the class DOES NOT use @OneToMany.
 * 
 * @author jules
 *
 */
@Entity
public class Category2 {

	@Id
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
