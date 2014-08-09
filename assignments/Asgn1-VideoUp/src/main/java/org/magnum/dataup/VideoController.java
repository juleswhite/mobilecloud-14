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

import java.util.List;

import org.magnum.dataup.model.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VideoController {

	private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
	
	@RequestMapping(value="/video", method=RequestMethod.GET)
	public List<Video> getVideos() {
		logger.debug("Retrieving videos");
		return null;
	}
	
	@RequestMapping(value="/video", method=RequestMethod.POST)
	public void addVideo(
			Video v
			){
		logger.debug("Adding video");
		
	}
	
	@RequestMapping(value="/video/{id}/data", method=RequestMethod.POST)
	public void uploadVideo(){
		logger.debug("Uploading video");
	}
	
	
	@RequestMapping(value="/video/{id}/data", method=RequestMethod.GET)
	public void downloadVideo(){
		logger.debug("Downloading video");
	}
	
}
