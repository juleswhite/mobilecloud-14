/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

/**
 *                       DO NOT MODIFY THIS INTERFACE
                    ___                    ___           ___                            
     _____         /\  \                  /\  \         /\  \                           
    /::\  \       /::\  \                 \:\  \       /::\  \         ___              
   /:/\:\  \     /:/\:\  \                 \:\  \     /:/\:\  \       /\__\             
  /:/  \:\__\   /:/  \:\  \            _____\:\  \   /:/  \:\  \     /:/  /             
 /:/__/ \:|__| /:/__/ \:\__\          /::::::::\__\ /:/__/ \:\__\   /:/__/              
 \:\  \ /:/  / \:\  \ /:/  /          \:\~~\~~\/__/ \:\  \ /:/  /  /::\  \              
  \:\  /:/  /   \:\  /:/  /            \:\  \        \:\  /:/  /  /:/\:\  \             
   \:\/:/  /     \:\/:/  /              \:\  \        \:\/:/  /   \/__\:\  \            
    \::/  /       \::/  /                \:\__\        \::/  /         \:\__\           
     \/__/         \/__/                  \/__/         \/__/           \/__/           
      ___           ___                                     ___                         
     /\  \         /\  \         _____                     /\__\                        
    |::\  \       /::\  \       /::\  \       ___         /:/ _/_         ___           
    |:|:\  \     /:/\:\  \     /:/\:\  \     /\__\       /:/ /\__\       /|  |          
  __|:|\:\  \   /:/  \:\  \   /:/  \:\__\   /:/__/      /:/ /:/  /      |:|  |          
 /::::|_\:\__\ /:/__/ \:\__\ /:/__/ \:|__| /::\  \     /:/_/:/  /       |:|  |          
 \:\~~\  \/__/ \:\  \ /:/  / \:\  \ /:/  / \/\:\  \__  \:\/:/  /      __|:|__|          
  \:\  \        \:\  /:/  /   \:\  /:/  /   ~~\:\/\__\  \::/__/      /::::\  \          
   \:\  \        \:\/:/  /     \:\/:/  /       \::/  /   \:\  \      ~~~~\:\  \         
    \:\__\        \::/  /       \::/  /        /:/  /     \:\__\          \:\__\        
     \/__/         \/__/         \/__/         \/__/       \/__/           \/__/        
 */
import java.util.Collection;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

/**
 * This interface defines an API for a VideoSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * interface into a client capable of sending the appropriate
 * HTTP requests.
 * 
 * The HTTP API that you must implement so that this interface
 * will work:
 * 
 * GET /video
 *   - Returns the list of videos that have been added to the
 *     server as JSON. The list of videos does not have to be
 *     persisted across restarts of the server. The list of
 *     Video objects should be able to be unmarshalled by the
 *     client into a Collection<Video>.
 *     
 * POST /video
 *   - The video data is provided as an application/json request
 *     body. The JSON should generate a valid instance of the 
 *     Video class when deserialized by Spring's default 
 *     Jackson library.
 *   - Returns the JSON representation of the Video object that
 *     was stored along with any updates to that object. 
 *     --The server should generate a unique identifier for the Video
 *     object and assign it to the Video by calling its setId(...)
 *     method. The returned Video JSON should include this server-generated
 *     identifier so that the client can refer to it when uploading the
 *     binary mpeg video content for the Video.
 *    -- The server should also generate a "data url" for the
 *     Video. The "data url" is the url of the binary data for a
 *     Video (e.g., the raw mpeg data). The URL should be the *full* URL
 *     for the video and not just the path. You can use a method like the
 *     following to figure out the name of your server:
 *     
 *     	private String getUrlBaseForLocalServer() {
 *		   HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
 *		   String base = "http://"+request.getServerName()+((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
 *		   return base;
 *		}
 *     
 * POST /video/{id}/data
 *   - The binary mpeg data for the video should be provided in a multipart
 *     request as a part with the key "data". The id in the path should be
 *     replaced with the unique identifier generated by the server for the
 *     Video. A client MUST *create* a Video first by sending a POST to /video
 *     and getting the identifier for the newly created Video object before
 *     sending a POST to /video/{id}/data. 
 *     
 * GET /video/{id}/data
 *   - Returns the binary mpeg data (if any) for the video with the given
 *     identifier. If no mpeg data has been uploaded for the specified video,
 *     then the server should return a 404 status code.
 *     
 *     
 * The VideoSvcApi interface described below should be used as the ultimate ground
 * truth for what should be implemented in the assignment. If there are any details
 * in the description above that conflict with the VideoSvcApi interface below, use
 * the details in the VideoSvcApi interface and report the discrepancy on the course
 * forums. 
 * 
 * For the ultimate ground truth of how the assignment will be graded, please see 
 * AutoGradingTest, which shows the specific tests that will be run to grade your
 * solution. 
 *   
 * @author jules
 *
 */
public interface VideoSvcApi {

	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";

	public static final String VIDEO_SVC_PATH = "/video";
	
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";

	/**
	 * This endpoint in the API returns a list of the videos that have
	 * been added to the server. The Video objects should be returned as
	 * JSON. 
	 * 
	 * To manually test this endpoint, run your server and open this URL in a browser:
	 * http://localhost:8080/video
	 * 
	 * @return
	 */
	@GET(VIDEO_SVC_PATH)
	public Collection<Video> getVideoList();
	
	/**
	 * This endpoint allows clients to add Video objects by sending POST requests
	 * that have an application/json body containing the Video object information. 
	 * 
	 * @return
	 */
	@POST(VIDEO_SVC_PATH)
	public Video addVideo(@Body Video v);
	
	/**
	 * This endpoint allows clients to set the mpeg video data for previously
	 * added Video objects by sending multipart POST requests to the server.
	 * The URL that the POST requests should be sent to includes the ID of the
	 * Video that the data should be associated with (e.g., replace {id} in
	 * the url /video/{id}/data with a valid ID of a video, such as /video/1/data
	 * -- assuming that "1" is a valid ID of a video). 
	 * 
	 * @return
	 */
	@Multipart
	@POST(VIDEO_DATA_PATH)
	public VideoStatus setVideoData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile videoData);
	
	/**
	 * This endpoint should return the video data that has been associated with
	 * a Video object or a 404 if no video data has been set yet. The URL scheme
	 * is the same as in the method above and assumes that the client knows the ID
	 * of the Video object that it would like to retrieve video data for.
	 * 
	 * This method uses Retrofit's @Streaming annotation to indicate that the
	 * method is going to access a large stream of data (e.g., the mpeg video 
	 * data on the server). The client can access this stream of data by obtaining
	 * an InputStream from the Response as shown below:
	 * 
	 * VideoSvcApi client = ... // use retrofit to create the client
	 * Response response = client.getData(someVideoId);
	 * InputStream videoDataStream = response.getBody().in();
	 * 
	 * @param id
	 * @return
	 */
	@Streaming
    @GET(VIDEO_DATA_PATH)
    Response getData(@Path(ID_PARAMETER) long id);
	
}
