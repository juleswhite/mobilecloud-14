/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.videoup.client.unsafe;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;


/**
 * Easy to use Http and Https client, that transparently adds gzip compression
 * and ignores all Https certificates. It can also be used for using credentials
 * in your connection.
 * 
 * This class was created for Android applications, where the appropriate apache libraries
 * are already available. If you are developing for another platform, make sure
 * to add the httpclient, httpcore and commons-logging libs to your buildpath. 
 * They can be downloaded from http://hc.apache.org/downloads.cgi
 * 
 *  <code><br/>
		EasyHttpClient client = new EasyHttpClient();<br/>
		System.out.println(client.get("https://encrypted.google.com/"));<br/>
 *  </code>
 * 
 * @author match2blue software development GmbH
 * @author Ren� Fischer, Ulrich Scheller
 */
public class EasyHttpClient extends DefaultHttpClient {
	/**
	 * Default http port
	 */
	private final static int HTTP_PORT = 80;

	/**
	 * Default https port
	 */	
	private final static int HTTPS_PORT = 443;
	
	protected int lastStatusCode;

	protected String lastReasonPhrase;
	
	/**
	 * Default constructor that initializes gzip handling. It adds the 
	 * Accept-Encoding gzip flag and also decompresses the response from the server. 
	 */
	public EasyHttpClient() {
		addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(final HttpRequest request,
					final HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});

		addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					for (HeaderElement headerElement : ceheader.getElements()) {
						if (headerElement.getName().equalsIgnoreCase("gzip")) {
							response.setEntity(new GzipEntityWrapper(response.getEntity()));
							lastStatusCode = response.getStatusLine().getStatusCode();
							lastReasonPhrase = response.getStatusLine().getReasonPhrase();
							return;
						}
					}
				}
			}
		});
	}
	
	/**
	 * Constructor which handles credentials for the connection (only if username and password are set)
	 * 
	 * @param username
	 * @param password
	 */
	public EasyHttpClient(String username, String password) {
		if(username != null && password != null) {			
		    UsernamePasswordCredentials c = new UsernamePasswordCredentials(username,password);
		    BasicCredentialsProvider cP = new BasicCredentialsProvider(); 
		    cP.setCredentials(AuthScope.ANY, c); 
		    setCredentialsProvider(cP);
		}
	}
	
	/**
	 * Function that creates a ClientConnectionManager which can handle http and https. 
	 * In case of https self signed or invalid certificates will be accepted.
	 */
	@Override
	protected ClientConnectionManager createClientConnectionManager() {		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);
		
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
		registry.register(new Scheme("https", new EasySSLSocketFactory(), HTTPS_PORT));
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
					
		return manager;
	}
	
	/**
	 * Make a get request to the specified url
	 * @param url
	 * @return the response string, null if there was an error
	 */
	public String get(String url) {
		HttpGet getReq = new HttpGet(url);
		InputStream content = null;
		try {
			content = execute(getReq).getEntity().getContent();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = content.read(buf)) > 0) {
				bout.write(buf, 0, len);
			}
			content.close();
			return bout.toString();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		EasyHttpClient client = new EasyHttpClient();
		System.out.println(client.get("https://encrypted.google.com/"));		
	}
}

class GzipEntityWrapper extends HttpEntityWrapper {
	public GzipEntityWrapper(HttpEntity wrapped) {
		super(wrapped);
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		return new GZIPInputStream(wrappedEntity.getContent());
	}
}


/**
 * This socket factory will create ssl socket that accepts self signed
 * certificate
 */
class EasySSLSocketFactory implements SocketFactory, LayeredSocketFactory {
	private SSLContext sslcontext = null;

	private static SSLContext createEasySSLContext() throws IOException {
		try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null,
					new TrustManager[] { new TrivialTrustManager() }, null);
			return context;
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	private SSLContext getSSLContext() throws IOException {
		if (this.sslcontext == null) {
			this.sslcontext = createEasySSLContext();
		}
		return this.sslcontext;
	}

	/**
	 * @see org.apache.http.conn.scheme.SocketFactory#connectSocket(java.net.Socket,
	 *      java.lang.String, int, java.net.InetAddress, int,
	 *      org.apache.http.params.HttpParams)
	 */
	public Socket connectSocket(Socket sock, String host, int port,
			InetAddress localAddress, int localPort, HttpParams params)
			throws IOException, UnknownHostException, ConnectTimeoutException {
		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);

		InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
		SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

		if ((localAddress != null) || (localPort > 0)) {
			// we need to bind explicitly
			if (localPort < 0) {
				localPort = 0; // indicates "any"
			}
			InetSocketAddress isa = new InetSocketAddress(localAddress,
					localPort);
			sslsock.bind(isa);
		}

		sslsock.connect(remoteAddress, connTimeout);
		sslsock.setSoTimeout(soTimeout);
		return sslsock;
	}

	/**
	 * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
	 */
	public Socket createSocket() throws IOException {
		return getSSLContext().getSocketFactory().createSocket();
	}

	/**
	 * @see org.apache.http.conn.scheme.SocketFactory#isSecure(java.net.Socket)
	 */
	public boolean isSecure(Socket socket) throws IllegalArgumentException {
		return true;
	}

	/**
	 * @see org.apache.http.conn.scheme.LayeredSocketFactory#createSocket(java.net.Socket,
	 *      java.lang.String, int, boolean)
	 */
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(socket, host,
				port, autoClose);
	}

	// -------------------------------------------------------------------
	// javadoc in org.apache.http.conn.scheme.SocketFactory says :
	// Both Object.equals() and Object.hashCode() must be overridden
	// for the correct operation of some connection managers
	// -------------------------------------------------------------------

	public boolean equals(Object obj) {
		return ((obj != null) && obj.getClass() == this.getClass());
	}

	public int hashCode() {
		return EasySSLSocketFactory.class.hashCode();
	}
}


class TrivialTrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}