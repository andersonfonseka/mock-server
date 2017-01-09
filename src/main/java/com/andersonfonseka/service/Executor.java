package com.andersonfonseka.service;

import com.andersonfonseka.domain.Operation;

public abstract class Executor {
	
	String params;
	String method;
	String data;

	public Executor(String params, String method, String... data) {
		super();
		this.params = params;
		this.method = method;
		
		if (data.length > 0){
			this.data = data[0];	
		}
	}

	public String getParams() {
		return params;
	}

	public String getMethod() {
		return method;
	}

	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	abstract String perform(String path, String data, Operation op);

}
