/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.videoup.client;

import org.magnum.videoup.client.oauth.SecuredRestBuilder;
import org.magnum.videoup.client.unsafe.EasyHttpClient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;

public class VideoSvc {

	public static final String CLIENT_ID = "mobile";

	private static VideoSvcApi videoSvc_;

	public static synchronized VideoSvcApi getOrShowLogin(Context ctx) {
		if (videoSvc_ != null) {
			return videoSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized VideoSvcApi init(String server, String user,
			String pass) {

		videoSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + VideoSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(VideoSvcApi.class);

		return videoSvc_;
	}
}
