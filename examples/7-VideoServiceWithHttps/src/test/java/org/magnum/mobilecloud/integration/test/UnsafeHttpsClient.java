package org.magnum.mobilecloud.integration.test;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This is an example of an HTTP client that does not properly
 * validate SSL certificates that are used for HTTPS. You should
 * NEVER use a client like this in a production application. Self-signed
 * certificates are ususally only OK for testing purposes, such as
 * this use case. 
 * 
 * @author jules
 *
 */
public class UnsafeHttpsClient {

	public static HttpClient createUnsafeClient() {
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					builder.build());
			CloseableHttpClient httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf).build();

			return httpclient;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
