package com.andersonfonseka.domain;

import java.util.ArrayList;
import java.util.List;

public class Service {
	
	private String url;
	
	private List<Operation> operations = new ArrayList<Operation>();
	
	public void addOperation(Operation operation){
		this.operations.add(operation);
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
