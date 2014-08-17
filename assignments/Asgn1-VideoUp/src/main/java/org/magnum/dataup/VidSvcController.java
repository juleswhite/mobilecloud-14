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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import antlr.collections.List;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

@Controller
public class VidSvcController implements VideoSvcApi{
//
//	@RequestMapping("/videos")
//	
//	public @ResponseBody String ShowVideoList()
//	{
//		System.out.println("List of videos");
//		return "List of videos";
//	}
//	
//	@RequestMapping("/videos/add")
//	@ResponseBody
//	public boolean UploadVideo(
//		@RequestParam("data") MultipartFile videoData
//	)
//	{
//		try {
//			Video v = new Video();
//			InputStream in = videoData.getInputStream();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return true;
//	}

	private HashMap<Long,Video> VideoList = new HashMap<Long,Video>();
	
	@Override
	@RequestMapping(value="/video",method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList() {
		return VideoList.values();
	}

	@Override
	@RequestMapping(value="/video",method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		VideoList.put(v.getId(), v);
		return v;
	}

	@Override
	@RequestMapping(value="/video/{id}/data",method=RequestMethod.POST)
	public @ResponseBody VideoStatus setVideoData(
			@RequestParam("id") long id, 
			@RequestBody TypedFile videoData) 
	{
		VideoFileManager vfm;
		VideoStatus status = new VideoStatus(VideoState.PROCESSING);
		try {
			vfm = VideoFileManager.get();
			vfm.saveVideoData(VideoList.get(id), videoData.in());
			status.setState(VideoState.READY);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return status;
	}

	@Override
	@RequestMapping(value="/video/{id}/data",method=RequestMethod.GET)
	public Response getData(
			@RequestParam("id") long id) 
	{
		TypedFile videoData;
//		videoData.
		Response resp = new Response(url, status, reason, headers, body);
//		resp.
		try {
			VideoFileManager vfm = VideoFileManager.get();
//			vfm.get
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		vfm.
		return videoData;
	}
}
