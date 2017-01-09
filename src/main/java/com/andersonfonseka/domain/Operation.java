package com.andersonfonseka.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.andersonfonseka.gui.ServerGUI;

public class Operation {

	private String id;
	
	private String name;
	
	private Request request;
	
	private ResponseFile response;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getResponse(String path, String method){

		String jsonObject = null;
		try {
			if (this.response.getPayload() != null && this.response.getPayload().trim().length() > 0){
				jsonObject = this.response.getPayload();
			} else {
				jsonObject = new ObjectMapper().writeValueAsString(this.response.getMapAttributes());	
			}
			
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		StringWriter content = new StringWriter();
		
		File file = new File(ServerGUI.GLOBAL_WADL_PATH + "/fakeServices/" + path.replace("?", "/") + "/" + method + "/" +  this.response.getName() + ".json");

		try {
		
			if (file.exists() && !ServerGUI.GLOBAL_FORWARD_MODE){
					IOUtils.copy(new FileInputStream(file), content);
			} else {
				
				File newFile = new File(ServerGUI.GLOBAL_WADL_PATH + "/fakeServices/" + path.replace("?", "/") + "/" + method);
				newFile.mkdirs();
				
				FileOutputStream out = new FileOutputStream(file);
				out.write(jsonObject.toString().getBytes());
				out.close();
				
				content.write(jsonObject.toString());
			} 
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content.toString();
		
	}

	public ResponseFile getResponseFile() {
		return response;
	}

	public void setResponse(ResponseFile response) {
		this.response = response;
	}
	
}
