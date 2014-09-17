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

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class VideoController {

    private Map<Long, Video> videos = new HashMap<>();
    private static final AtomicLong currentId = new AtomicLong(0L);

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideoList() {
        return videos.values();
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video v) {
        checkAndSetId(v);
        v.setDataUrl(getDataUrl(v.getId()));
        videos.put(v.getId(), v);
        return v;
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_DATA_PATH, method = RequestMethod.POST)
    public @ResponseBody VideoStatus setVideoData(@PathVariable(VideoSvcApi.ID_PARAMETER) long id,
            @RequestParam(VideoSvcApi.DATA_PARAMETER) MultipartFile videoData)
            throws NoSuchRequestHandlingMethodException {
        if (!videos.containsKey(id)) {
            throw new NoSuchRequestHandlingMethodException("setVideoData", this.getClass());
        }
        try {
            final VideoFileManager videoFileManager = VideoFileManager.get();
            videoFileManager.saveVideoData(videos.get(id), videoData.getInputStream());
            return new VideoStatus(VideoStatus.VideoState.READY);
        } catch (IOException e) {
            throw new NoSuchRequestHandlingMethodException("setVideoData", this.getClass());
        }
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_DATA_PATH, method = RequestMethod.GET)
    public @ResponseBody HttpServletResponse getData(@PathVariable(VideoSvcApi.ID_PARAMETER) long id,
            HttpServletResponse response) throws NoSuchRequestHandlingMethodException {
        if (!videos.containsKey(id)) {
            throw new NoSuchRequestHandlingMethodException("getData", this.getClass());
        }
        try {
            final VideoFileManager videoFileManager = VideoFileManager.get();
            videoFileManager.copyVideoData(videos.get(id), response.getOutputStream());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            throw new NoSuchRequestHandlingMethodException("getData", this.getClass());
        }
        return null;
    }

    private void checkAndSetId(Video entity) {
        if (entity.getId() == 0) {
            entity.setId(currentId.incrementAndGet());
        }
    }

    private String getDataUrl(long videoId) {
        return getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
    }

    private String getUrlBaseForLocalServer() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return "http://" + request.getServerName()
                + ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
    }
}
