package com.andersonfonseka.domain;

import java.util.ArrayList;
import java.util.List;

public class Request {
	
	private List<Param> params = new ArrayList<Param>();
	
	public void addParam(Param param){
		this.params.add(param);
	}

	public List<Param> getParams() {
		return params;
	}
	
}
