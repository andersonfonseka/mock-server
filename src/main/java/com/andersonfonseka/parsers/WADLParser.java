package com.andersonfonseka.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.andersonfonseka.domain.Operation;
import com.andersonfonseka.domain.Service;
import com.andersonfonseka.gui.ServerGUI;
import com.andersonfonseka.monitor.ProgressMonitor;

public class WADLParser {
	
	private ProgressMonitor progressMonitor;
	
	public WADLParser(ProgressMonitor progressMonitor) {
		super();
		this.progressMonitor = progressMonitor;
	}

	public Map<String, Service> parse(String fileName) throws ParserConfigurationException, SAXException, IOException{
		
		Map<String, Service> services = new HashMap<String, Service>();
		Map<String, Service> realServices = new HashMap<String, Service>();
		
		File file = new File(fileName);
		
		if (file.exists()){
			InputStream inputStream = new FileInputStream(file);
			SAXParserFactory spf = SAXParserFactory.newInstance();
		    spf.setNamespaceAware(true);
		    SAXParser saxParser = spf.newSAXParser();
		    saxParser.parse(inputStream, new WADLHandler(this.progressMonitor, services, file));
		}
		
		Iterator<String> it = services.keySet().iterator();
		
		while(it.hasNext()){
			
			String key = it.next();
			
			if (services.containsKey(key) && !services.get(key).getOperations().isEmpty()){
				realServices.put(key, services.get(key));
				
				Service service = services.get(key);
				
				for (Operation operation: service.getOperations()) {
					File directories = new File(ServerGUI.GLOBAL_WADL_PATH + "/fakeServices/" + key + "/" + operation.getName());
					directories.mkdirs();
				} 
			}
		}
		
		return realServices;
	}

	
	
}
