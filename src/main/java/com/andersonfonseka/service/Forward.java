package com.andersonfonseka.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.andersonfonseka.monitor.Message;
import com.andersonfonseka.monitor.ProgressMonitor;

public class Forward {

	private ProgressMonitor progressMonitor;
	
	public Forward(ProgressMonitor progressMonitor) {
		super();
		this.progressMonitor = progressMonitor;
	}

	public String result(HttpHeaders headers, String url, String method, String...payload) throws Exception {

		HttpClient client = HttpClientBuilder.create().build();
		
		HttpRequestBase request = null;
		
		if (method.equals("GET")){
			request = new HttpGet(url);	
		} else if (method.equals("POST")){
			HttpPost requestPost = new HttpPost(url);
			requestPost.setEntity(new StringEntity(payload[0]));
			request = requestPost;
		} else if (method.equals("PUT")){
			HttpPut requestPut = new HttpPut(url);
			requestPut.setEntity(new StringEntity(payload[0]));
			request = requestPut;
		} else if (method.equals("DELETE")){
			HttpDelete requestDelete = new HttpDelete(url);
			request = requestDelete;
		}
		
		Iterator<String> it =  headers.getRequestHeaders().keySet().iterator();
		
//		while(it.hasNext()){
//			String key = it.next();
//			if (!key.equals("Content-Length")){
//				request.addHeader(key, headers.getHeaderString(key));	
//			}
//		}
		
		HttpResponse response = client.execute(request);

		this.progressMonitor.addMessage(new Message(Message.GENERIC, "Response Code : " + response.getStatusLine()));
		
		Header[] headersResponse = response.getAllHeaders();
		for (int i = 0; i < headersResponse.length; i++) {
			System.out.println(headersResponse[i].getName() + " - " + headersResponse[i].getValue());
		}
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			result.append(line);
		}
		
		return result.toString();

	}

}
