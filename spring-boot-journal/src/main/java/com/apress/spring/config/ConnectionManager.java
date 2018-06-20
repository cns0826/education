package com.apress.spring.config;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Configuration;


import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.HttpClient;






@Configuration 
public class ConnectionManager {

	private static PoolingClientConnectionManager connectionManager = null;
	
	public static synchronized HttpClient getHttpClient() {
		if (connectionManager == null) {
			connectionManager = new PoolingClientConnectionManager();
			connectionManager.setMaxTotal(8);
			connectionManager.setDefaultMaxPerRoute(4);
		}

		return new DefaultHttpClient(connectionManager);
	}

	public static void abort(HttpRequestBase httpRequest) {
		if (httpRequest != null) {
			try {
				httpRequest.abort();
			} catch (Exception e) {}
		}
	}

	public static void release(HttpResponse response) {
		if (response != null && response.getEntity() != null)
			EntityUtils.consumeQuietly(response.getEntity());
	}
}


