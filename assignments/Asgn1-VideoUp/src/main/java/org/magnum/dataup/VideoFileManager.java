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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.magnum.dataup.model.Video;

/**
 * This class provides a simple implementation to store video binary
 * data on the file system in a "videos" folder. The class provides
 * methods for saving videos and retrieving their binary data.
 * 
 * @author jules
 *
 */
public class VideoFileManager {

	/**
	 * This static factory method creates and returns a 
	 * VideoFileManager object to the caller. Feel free to customize
	 * this method to take parameters, etc. if you want.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static VideoFileManager get() throws IOException {
		return new VideoFileManager();
	}
	
	private Path targetDir_ = Paths.get("videos");
	
	// The VideoFileManager.get() method should be used
	// to obtain an instance
	private VideoFileManager() throws IOException{
		if(!Files.exists(targetDir_)){
			Files.createDirectories(targetDir_);
		}
	}
	
	// Private helper method for resolving video file paths
	private Path getVideoPath(Video v){
		assert(v != null);
		
		return targetDir_.resolve("video"+v.getId()+".mpg");
	}
	
	/**
	 * This method returns true if the specified Video has binary
	 * data stored on the file system.
	 * 
	 * @param v
	 * @return
	 */
	public boolean hasVideoData(Video v){
		Path source = getVideoPath(v);
		return Files.exists(source);
	}
	
	/**
	 * This method copies the binary data for the given video to
	 * the provided output stream. The caller is responsible for
	 * ensuring that the specified Video has binary data associated
	 * with it. If not, this method will throw a FileNotFoundException.
	 * 
	 * @param v 
	 * @param out
	 * @throws IOException 
	 */
	public void copyVideoData(Video v, OutputStream out) throws IOException {
		Path source = getVideoPath(v);
		if(!Files.exists(source)){
			throw new FileNotFoundException("Unable to find the referenced video file for videoId:"+v.getId());
		}
		Files.copy(source, out);
	}
	
	/**
	 * This method reads all of the data in the provided InputStream and stores
	 * it on the file system. The data is associated with the Video object that
	 * is provided by the caller.
	 * 
	 * @param v
	 * @param videoData
	 * @throws IOException
	 */
	public void saveVideoData(Video v, InputStream videoData) throws IOException{
		assert(videoData != null);
		
		Path target = getVideoPath(v);
		Files.copy(videoData, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
}
