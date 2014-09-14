/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mobilecloud.video;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
@EnableWebMvc
public class Application extends WebMvcConfigurerAdapter {

	// We do not have the typical main method because we need
	// the Maven AppEngine plugin to launch / configure the
	// development server. However, we are still using this
	// class to define configuration information.

}
