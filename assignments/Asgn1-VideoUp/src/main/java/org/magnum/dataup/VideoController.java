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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.magnum.dataup.model.Video;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
		if(v != null) {
			v.setId(id.getAndIncrement());
			v.setDataUrl("url");
			if(videos.add(v)) {
				return v;
			}
		}
		return v;
	}
}
