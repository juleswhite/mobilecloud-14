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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VideoController {

	private List<Video> videos = new ArrayList<>();

	private AtomicLong id = new AtomicLong(1);

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList() {
		return videos;
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		if (v != null) {
			v.setId(id.getAndIncrement());
			v.setDataUrl(getDataUrl(v.getId()));
			if (videos.add(v)) {
				return v;
			}
		}
		return v;
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_DATA_PATH, method = RequestMethod.GET)
	public void getData(@PathVariable("id") long videoId, HttpServletResponse response) {
		try {
			Video video = null;
			for (int i = 0; i < videos.size(); i++) {
				if (Long.compare(videos.get(i).getId(), videoId) == 0) {
					video = videos.get(i);
					if (!VideoFileManager.get().hasVideoData(video)) {
						throw new ResourceNotFoundException();
					}

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					VideoFileManager.get().copyVideoData(video, out);
					response.getOutputStream().write(out.toByteArray());
				}
			}
			if(video == null) {
				throw new ResourceNotFoundException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@RequestMapping(value = VideoSvcApi.VIDEO_DATA_PATH, method = RequestMethod.POST)
	public @ResponseBody VideoStatus setVideoData(@PathVariable("id") long videoId, @RequestPart(VideoSvcApi.DATA_PARAMETER) MultipartFile videoData, 
			HttpServletResponse response) {
		Video video = null;
		for (int i = 0; i < videos.size(); i++) {
			if (Long.compare(videos.get(i).getId(), videoId) == 0) {
				video = videos.get(i);
			}
		}
		if(video == null) {
			throw new ResourceNotFoundException();
		}
		
		try {
			VideoFileManager.get().saveVideoData(video, videoData.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException("Cannot save video data");
		}
		
		return new VideoStatus(VideoState.READY);
	}

	private String getDataUrl(long videoId){
        String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
        return url;
    }

	private String getUrlBaseForLocalServer() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String base = "http://"
				+ request.getServerName()
				+ ((request.getServerPort() != 80) ? ":"
						+ request.getServerPort() : "");
		return base;
	}
	
	@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND)
	public static final class ResourceNotFoundException extends
			RuntimeException {
		private static final long serialVersionUID = -8916205905961324734L;
	}
	
}
