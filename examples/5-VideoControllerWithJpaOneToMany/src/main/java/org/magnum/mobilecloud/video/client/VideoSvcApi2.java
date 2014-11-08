package org.magnum.mobilecloud.video.client;

import java.util.Collection;

import org.magnum.mobilecloud.video.repositorywithoutonetomany.Category2;
import org.magnum.mobilecloud.video.repositorywithoutonetomany.Video2;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc that does not
 * use the @OneToMany annotation but provides the same functionality. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 *
 */
public interface VideoSvcApi2 {
	
	public static final String TITLE_PARAMETER = "title";

	// The path where we expect the VideoSvc to live
	public static final String VIDEO_SVC_PATH = "/video";
	
	// The path were we expect Categories to live
	public static final String CATEGORY_SVC_PATH = "/category";

	// The path to search videos by title
	public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH + "/find";

	@GET(VIDEO_SVC_PATH)
	public Collection<Video2> getVideoList();
	
	@POST(VIDEO_SVC_PATH)
	public boolean addVideo(@Body Video2 v);
	
	@GET(VIDEO_TITLE_SEARCH_PATH)
	public Collection<Video2> findByTitle(@Query(TITLE_PARAMETER) String title);
	
	@GET(VIDEO_SVC_PATH+"/{category}")
	public Collection<Video2> getVideoListForCategory(@Path("category") String categoryName);
	
	@POST(CATEGORY_SVC_PATH)
	public boolean addCategory(@Body Category2 c);
	
}
