package org.magnum.dataup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.stereotype.Service;

import retrofit.client.Response;
import retrofit.mime.TypedFile;

@Service
public class VideoSvc implements VideoSvcApi {
	
	private List<Video> videos = new ArrayList<>();

	@Override
	public Collection<Video> getVideoList() {
		return videos;
	}

	@Override
	public Video addVideo(Video v) {
		if(videos.add(v)) {
			return v;
		}
		return null;
	}

	@Override
	public VideoStatus setVideoData(long id, TypedFile videoData) {
		return null;
	}

	@Override
	public Response getData(long id) {
		return null;
	}

}
