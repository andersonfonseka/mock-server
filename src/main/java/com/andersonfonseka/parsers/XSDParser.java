package com.andersonfonseka.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.andersonfonseka.domain.ResponseFile;
import com.andersonfonseka.monitor.Message;
import com.andersonfonseka.monitor.ProgressMonitor;

public class XSDParser {
	
	private Map<String, ResponseFile> responseFiles;
	
	private ProgressMonitor progressMonitor;
	
	public XSDParser(ProgressMonitor progressMonitor) {
		super();
		this.progressMonitor = progressMonitor;
	}

	public void parse(String fileName, File directory, boolean checkParent, Map<String, ResponseFile> responseFiles) 
																	throws ParserConfigurationException, SAXException, IOException{
		
		File file = null;
		
		if (responseFiles == null){
			this.responseFiles = new HashMap<String, ResponseFile>();
		} else {
			this.responseFiles = responseFiles;
		}
		
		if (checkParent){
			file = new File(directory.getParent() + "/" + fileName);
		} else {
			file = new File(directory + "/" + fileName);
		}
		
		if (file.exists()){
			InputStream inputStream = new FileInputStream(file);
			SAXParserFactory spf = SAXParserFactory.newInstance();
		    spf.setNamespaceAware(true);
		    SAXParser saxParser = spf.newSAXParser();
		    saxParser.parse(inputStream, new XSDHandler(this.responseFiles, this.progressMonitor));
		    
		   // this.progressMonitor.addMessage(new Message(Message.GENERIC, file.getName() + " is found!"));
		} else {
		    this.progressMonitor.addMessage(new Message(Message.GENERIC, file.getName() + " was not found!"));
		}
		
	}

	public Map<String, ResponseFile> getResponseFiles() {
		return responseFiles;
	}

}
