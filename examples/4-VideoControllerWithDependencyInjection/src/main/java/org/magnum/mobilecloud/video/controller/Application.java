package org.magnum.mobilecloud.video.controller;

import org.magnum.mobilecloud.video.repository.NoDuplicatesVideoRepository;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// Tell Spring that this object represents a Configuration for the
// application
@Configuration
// Tell Spring to turn on WebMVC (e.g., it should enable the DispatcherServlet
// so that requests can be routed to our Controllers)
@EnableWebMvc
// Tell Spring to go and scan our controller package (and all sub packages) to
// find any Controllers or other components that are part of our applciation.
// Any class in this package that is annotated with @Controller is going to be
// automatically discovered and connected to the DispatcherServlet.
@ComponentScan("org.magnum.mobilecloud.video.controller")
// Tell Spring to automatically inject any dependencies that are marked in
// our classes with @Autowired
@EnableAutoConfiguration
public class Application {

	// Tell Spring to launch our app!
	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}
	
	// We need to tell Spring which implementation of the VideoRepository
	// that it should use. Spring is going to automatically inject whatever
	// we return into the VideoSvc's videos member variable that is annotated
	// with @Autowired.
	@Bean
	public VideoRepository videoRepository(){
		return new NoDuplicatesVideoRepository();
	}
	
}
