package com.andersonfonseka.domain;

import java.util.HashMap;
import java.util.Map;

public class ResponseFile {
	
	private String name;
	
	private Map<String, Object> mapAttributes = new HashMap<String, Object>();
	
	private String payload;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getMapAttributes() {
		return mapAttributes;
	}

	public void setMapAttributes(Map<String, Object> mapAttributes) {
		this.mapAttributes = mapAttributes;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
