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
package org.magnum.mobilecloud.video;

import io.magnum.autograder.HandinUtil;

import java.io.File;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHttpRequest;
import org.magnum.mobilecloud.integration.test.InternalAutoGradingTest;
import org.magnum.mobilecloud.integration.test.UnsafeHttpsClient;

/**
 * This class allows you to estimate the score for your solution before handing it in.
 * 
 * This class also generates the required submission package to hand your solution in.
 * 
 * In order to run this AutoGrading application, in Eclipse, Right-click on it->Run As->Java Application
 * 
 * 
 * Please read the instructions that the application prints very carefully. It will store the generated
 * solution submission packages in the coursera-submission folder within the project directory. After running
 * this application, you will need to right-click on the root of the project in Eclipse->Refresh to see this 
 * folder inside of Eclipse. Each time that you run this application, it generates a new unique submission
 * package. Make sure that you submit the correct (e.g., most up to date) package when you turn your assignment
 * in.
 * 
 * @author jules
 *
 *                       DO NOT MODIFY THIS CLASS
                    ___                    ___           ___                            
     _____         /\  \                  /\  \         /\  \                           
    /::\  \       /::\  \                 \:\  \       /::\  \         ___              
   /:/\:\  \     /:/\:\  \                 \:\  \     /:/\:\  \       /\__\             
  /:/  \:\__\   /:/  \:\  \            _____\:\  \   /:/  \:\  \     /:/  /             
 /:/__/ \:|__| /:/__/ \:\__\          /::::::::\__\ /:/__/ \:\__\   /:/__/              
 \:\  \ /:/  / \:\  \ /:/  /          \:\~~\~~\/__/ \:\  \ /:/  /  /::\  \              
  \:\  /:/  /   \:\  /:/  /            \:\  \        \:\  /:/  /  /:/\:\  \             
   \:\/:/  /     \:\/:/  /              \:\  \        \:\/:/  /   \/__\:\  \            
    \::/  /       \::/  /                \:\__\        \::/  /         \:\__\           
     \/__/         \/__/                  \/__/         \/__/           \/__/           
      ___           ___                                     ___                         
     /\  \         /\  \         _____                     /\__\                        
    |::\  \       /::\  \       /::\  \       ___         /:/ _/_         ___           
    |:|:\  \     /:/\:\  \     /:/\:\  \     /\__\       /:/ /\__\       /|  |          
  __|:|\:\  \   /:/  \:\  \   /:/  \:\__\   /:/__/      /:/ /:/  /      |:|  |          
 /::::|_\:\__\ /:/__/ \:\__\ /:/__/ \:|__| /::\  \     /:/_/:/  /       |:|  |          
 \:\~~\  \/__/ \:\  \ /:/  / \:\  \ /:/  / \/\:\  \__  \:\/:/  /      __|:|__|          
  \:\  \        \:\  /:/  /   \:\  /:/  /   ~~\:\/\__\  \::/__/      /::::\  \          
   \:\  \        \:\/:/  /     \:\/:/  /       \::/  /   \:\  \      ~~~~\:\  \         
    \:\__\        \::/  /       \::/  /        /:/  /     \:\__\          \:\__\        
     \/__/         \/__/         \/__/         \/__/       \/__/           \/__/        
*/
public class AutoGrading {


	public static void main(String[] args) throws Exception {

		// Ensure that this application is run within the correct working directory
		File f = new File("./src/main/java/org/magnum/mobilecloud/video/Application.java");
		if (!f.exists()) {
			System.out
					.println(WordUtils
							.wrap("You must run the AutoGrading application from the root of the "
									+ "project directory containing src/main/java. If you right-click->Run As->Java Application "
									+ "in Eclipse, it will automatically use the correct classpath and working directory "
									+ "(assuming that you have Gradle and the project setup correctly).",
									80));
			System.exit(1);
		}

		// Ensure that the server is running and accessible on port 8443
		try {
			HttpClient client = UnsafeHttpsClient.createUnsafeClient();
			HttpResponse response = 
					client.execute(new HttpHost("127.0.0.1", 8443), new BasicHttpRequest("GET", "/"));
			
			response.getStatusLine();
		} catch (NoHttpResponseException e) { 
			  // The server may have returned some JSON object
		}
		catch (Exception e) {
			System.out
					.println(WordUtils
							.wrap("Unable to connect to your server on https://localhost:8443. Are you sure the server is running? "
									+ "In order to run the autograder, you must first launch your application "
									+ "by right-clicking on the Application class in Eclipse, and"
									+ "choosing Run As->Java Application. If you have already done this, make sure that"
									+ " you can access your server by opening the https://localhost:8443 url in a browser. "
									+ "If you can't access the server in a browser, it probably indicates you have a firewall "
									+ "or some other issue that is blocking access to port 8080 on localhost.",
									80));
			System.exit(1);
		}
		
		HandinUtil.generateHandinPackage("Asgn2", new File("./"),
				InternalAutoGradingTest.class);
	}

}