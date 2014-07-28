package org.magnum.dataup.controller;

import org.magnum.dataup.VideoFileManager;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class VideoController {

    public static final String BASE_URL = "/video";

    private Video video = Video.create().withContentType("video/mp4")
            .withDuration(123).withSubject(UUID.randomUUID().toString())
            .withTitle(UUID.randomUUID().toString()).build();

    private HashMap<Long, Video> videos = new HashMap<Long, Video>();

    @RequestMapping(value = BASE_URL, method = RequestMethod.GET)
    public Collection<Video> getVideoList() {
        if (videos.size() == 0) {
            video.setId(videos.size() + 1);
            video.setDataUrl("http://localhost:8080/video/" + video.getId() + "/data");
            videos.put(video.getId(), video);
        }
        return videos.values();

    }

    @RequestMapping(value = BASE_URL, method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public Video addVideo(@RequestBody Video v) {
        v.setId(videos.size() + 1);
        v.setDataUrl("http://localhost:8080/video/" + v.getId() + "/data");
        videos.put(v.getId(), v);
        return v;
    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
    public
    @ResponseBody
    VideoStatus addVideoData(@PathVariable("id") String id, @RequestPart("data") MultipartFile data, HttpServletResponse response) {
        try {
            Long receivedId = new Long(id);
            if (videos.containsKey(new Long(receivedId))) {
                try {
                    VideoFileManager vfm = VideoFileManager.get();
                    Video v = videos.get(new Long(id));
                    vfm.saveVideoData(v, data.getInputStream());
                    return new VideoStatus(VideoStatus.VideoState.READY);

                } catch (IOException e) {
                    throw new ResourceNotFoundException("VideoFileManager not found");
                }
            } else {
                throw new ResourceNotFoundException("Video with id " + id + " not found.");
            }

        } catch (Exception e) {
            throw new ResourceNotFoundException("Video with id " + id + " not found.");
        }


    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
    public
    @ResponseBody
    void getData(@PathVariable("id") long id, HttpServletResponse response) {
        if (videos.containsKey(new Long(id))) {
            Video video = videos.get(new Long(id));
            response.addHeader("Content-Type", video.getContentType());
            try {
                VideoFileManager vfm = VideoFileManager.get();
                vfm.copyVideoData(video, response.getOutputStream());
            } catch (IOException e) {
                throw new ResourceNotFoundException("VideoFileManager not found");
            }
        } else {
            throw new ResourceNotFoundException("Video with id " + id + " not found.");
        }
    }
}
