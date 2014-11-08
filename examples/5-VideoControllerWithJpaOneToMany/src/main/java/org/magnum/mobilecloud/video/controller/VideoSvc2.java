package org.magnum.mobilecloud.video.controller;

import java.util.Collection;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.Category2;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.CategoryRepository2;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.Video2;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.VideoRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

/**
 * This simple VideoSvc allows clients to send HTTP POST requests with
 * videos that are stored in memory using a list. Clients can send HTTP GET
 * requests to receive a JSON listing of the videos that have been sent to
 * the controller so far. Stopping the controller will cause it to lose the history of
 * videos that have been sent to it because they are stored in memory.
 * 
 * Notice how much simpler this VideoSvc is than the original VideoServlet?
 * Spring allows us to dramatically simplify our service. Another important
 * aspect of this version is that we have defined a VideoSvcApi that provides
 * strong typing on both the client and service interface to ensure that we
 * don't send the wrong paraemters, etc.
 * 
 * This version of the VideoSvc does not use @OneToMany for any of its Video/Category
 * operations but provides identical functionality to VideoSvc.
 * 
 * @author jules
 *
 */

// Tell Spring that this class is a Controller that should 
// handle certain HTTP requests for the DispatcherServlet
@Controller
public class VideoSvc2 {
	
	// The VideoRepository that we are going to store our videos
	// in. We don't explicitly construct a VideoRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring. Our Application class has a method
	// annotated with @Bean that determines what object will end
	// up being injected into this member variable.
	//
	// Also notice that we don't even need a setter for Spring to
	// do the injection.
	//
	@Autowired
	private VideoRepository2 videos;
	
	@Autowired
	private CategoryRepository2 categories;

	// Receives POST requests to /video and converts the HTTP
	// request body, which should contain json, into a Video
	// object before adding it to the list. The @RequestBody
	// annotation on the Video parameter is what tells Spring
	// to interpret the HTTP request body as JSON and convert
	// it into a Video object to pass into the method. The
	// @ResponseBody annotation tells Spring to conver the
	// return value from the method back into JSON and put
	// it into the body of the HTTP response to the client.
	//
	// The VIDEO_SVC_PATH is set to "/video" in the VideoSvcApi
	// interface. We use this constant to ensure that the 
	// client and service paths for the VideoSvc are always
	// in synch.
	//
	@RequestMapping(value= "/2" + VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean addVideo(@RequestBody Video2 v){
		
		// The approach without @OneToMany requires us to do a little bit more
		// work to check and ensure that the specified Category actually exists
		// before we save a Video. If we use @OneToMany, JPA automatically ensures
		// that a Category referenced by a Video exists.
		if(categories.findOne(v.getCategory()) == null){
			throw new RuntimeException("Unknown category:"+v.getCategory());
		}
		
		 videos.save(v);
		 return true;
	}
	
	// Receives POST requests to /category and converts the HTTP
	// request body, which should contain json, into a Category
	// object before adding it to the list. The @RequestBody
	// annotation on the Category parameter is what tells Spring
	// to interpret the HTTP request body as JSON and convert
	// it into a Category object to pass into the method. The
	// @ResponseBody annotation tells Spring to convert the
	// return value from the method back into JSON and put
	// it into the body of the HTTP response to the client.
	//
	// The VIDEO_SVC_PATH is set to "/video" in the VideoSvcApi
	// interface. We use this constant to ensure that the 
	// client and service paths for the VideoSvc are always
	// in synch.
	//
	@RequestMapping(value= "/2" + VideoSvcApi.CATEGORY_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean addCategory(@RequestBody Category2 c){
		 categories.save(c);
		 return true;
	}
	
	
	// Receives GET requests to /video and returns the current
	// list of videos. Spring automatically converts
	// the list of videos to JSON because of the @ResponseBody
	// annotation.
	@RequestMapping(value= "/2" + VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video2> getVideoList(){
		return Lists.newArrayList(videos.findAll());
	}
	
	// Receives GET requests to /video/{category} and returns the current
	// list of videos that are part of the specified category.
	@RequestMapping(value= "/2" + VideoSvcApi.VIDEO_SVC_PATH+"/{category}", method=RequestMethod.GET)
	public @ResponseBody Collection<Video2> getVideoListForCategory(@PathVariable("category") String categoryName){
		return Lists.newArrayList(videos.findByCategory(categoryName));
	}
	
	
	// Receives GET requests to /video/find and returns all Videos
	// that have a title (e.g., Video.name) matching the "title" request
	// parameter value that is passed by the client
	@RequestMapping(value= "/2" + VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video2> findByTitle(
			// Tell Spring to use the "title" parameter in the HTTP request's query
			// string as the value for the title method parameter
			@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title
	){
		return videos.findByName(title);
	}

}
