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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VideoController {

	private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
	
	/**
	 * Used to generate unique id's to movies.
	 */
	private final AtomicLong idGenerator = new AtomicLong(0);
	
	/**
	 * A dictionary that holds video records.
	 */
	private Map<Long,Video> videos = new ConcurrentHashMap<>();
	
	@RequestMapping(value="/video", method=RequestMethod.GET)
	public @ResponseBody Collection<? extends Video> getVideos() {
		logger.debug("Retrieving videos");
		return videos.values();
	}
	
	@RequestMapping(value="/video", method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v){
		long id = idGenerator.incrementAndGet();
		String dataUrl = getDataUrl(id);
		if (v.getId() == 0){
			v.setId(id);
		}
		v.setDataUrl(dataUrl);
		videos.put(v.getId(), v);
		logger.debug("Successfully added meta data for video, id: {}", id);
		return v;
	}
	
	@RequestMapping(value="/video/{id}/data", method=RequestMethod.POST)
	public @ResponseBody VideoState uploadVideo(
			@RequestParam("id") Long id,
			@RequestParam("data") MultipartFile videoData,
			HttpServletResponse response){
		logger.debug("Starting to upload video data");
		if (!videos.containsKey(id)){
			response.setStatus(404);
			return null;
		}
		try(InputStream in = videoData.getInputStream()){
			//handle the input stream
			
		} catch (IOException e) {
			logger.error("Failed to upload a file to the server, due to:", e);
			response.setStatus(404);
		}
		return VideoState.READY;
	}
	
	
	@RequestMapping(value="/video/{id}/data", method=RequestMethod.GET)
	public void downloadVideo(){
		logger.debug("Downloading video");
	}
	
    private String getDataUrl(long videoId){
        String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
        return url;
    }
    
    
    private String getUrlBaseForLocalServer() {
        HttpServletRequest request = 
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String base = 
           "http://"+request.getServerName() 
           + ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
        return base;
     }
	
}
