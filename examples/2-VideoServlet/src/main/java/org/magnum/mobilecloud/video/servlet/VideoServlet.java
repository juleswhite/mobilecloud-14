package org.magnum.mobilecloud.video.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This simple VideoServlet allows clients to send HTTP POST
 * requests with videos that are stored in memory using a list.
 * Clients can send HTTP GET requests to receive a plain/test
 * listing of the videos that have been sent to the servlet
 * so far. Stopping the servlet will cause it to lose the history
 * of videos that have been sent to it because they are stored
 * in memory.
 * 
 * @author jules
 *
 */
public class VideoServlet extends HttpServlet // Servlets should inherit HttpServlet
{

	public static final String VIDEO_ADDED = "Video added.";
	// An in-memory list that the servlet uses to store the
	// videos that are sent to it by clients
	private List<Video> videos = new ArrayList<Video>();

	/**
	 * This method processes all of the HTTP GET requests routed to the
	 * servlet by the web container. This method loops through the lists
	 * of videos that have been sent to it and generates a plain/text 
	 * list of the videos that is sent back to the client.
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Make sure and set the content-type header so that the client
		// can properly (and securely!) display the content that you send
		// back
		resp.setContentType("text/plain");

		// This PrintWriter allows us to write data to the HTTP 
		// response body that is going to be sent to the client.
		PrintWriter sendToClient = resp.getWriter();
		
		// Loop through all of the stored videos and print them out
		// for the client to see.
		for (Video v : this.videos) {
			
			// For each video, write its name and URL into the HTTP
			// response body
			sendToClient.write(v.getName() + " : " + v.getUrl() + "\n");
		}

	}

	/**
	 * This method handles all HTTP POST requests that are routed to the
	 * servlet by the web container.
	 * 
	 * Sending a post to the servlet with 'name', 'duration', and 'url' 
	 * parameters causes a new video to be created and added to the list of 
	 * videos. 
	 * 
	 * If the client fails to send one of these parameters, the servlet generates 
	 * an HTTP error 400 (Bad request) response indicating that a required request
	 * parameter was missing.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// First, extract the HTTP request parameters that we are expecting
		// from either the URL query string or the url encoded form body
		String name = req.getParameter("name");
		String url = req.getParameter("url");
		String durationStr = req.getParameter("duration");
		
		// Check that the duration parameter provided by the client
		// is actually a number
		long duration = -1;
		try{
			duration = Long.parseLong(durationStr);
		}catch(NumberFormatException e){
			// The client sent us a duration value that wasn't a number!
		}

		// Make sure and set the content-type header so that the client knows
		// how to interpret the data that gets sent back
		resp.setContentType("text/plain");

		// Now, the servlet has to look at each request parameter that the
		// client was expected to provide and make sure that it isn't null,
		// empty, etc.
		if (name == null || url == null || durationStr == null
				|| name.trim().length() < 1 || url.trim().length() < 10
				|| durationStr.trim().length() < 1
				|| duration <= 0) {
			
			// If the parameters pass our basic validation, we need to 
			// send an HTTP 400 Bad Request to the client and give it
			// a hint as to what it got wrong.
			resp.sendError(400, "Missing ['name','duration','url'].");
		} 
		else {
			// It looks like the client provided all of the data that
			// we need, use that data to construct a new Video object
			Video v = new Video(name, url, duration);
			
			// Add the video to our in-memory list of videos
			videos.add(v);
			
			// Let the client know that we successfully added the video
			// by writing a message into the HTTP response body
			resp.getWriter().write(VIDEO_ADDED);
		}
	}

}
