package com.andersonfonseka.parsers;

import java.io.File;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.andersonfonseka.domain.Attribute;
import com.andersonfonseka.domain.ResponseFile;
import com.andersonfonseka.gui.ServerGUI;
import com.andersonfonseka.monitor.ProgressMonitor;

public class XSDHandler extends DefaultHandler {
	
	private ResponseFile responseFile;
	
	private Map<String, ResponseFile> responseFiles;
	
	private ProgressMonitor progressMonitor;
	
	public XSDHandler(Map<String, ResponseFile> responseFiles, ProgressMonitor progressMonitor) {
		super();
		this.responseFiles = responseFiles;
		this.progressMonitor = progressMonitor;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		
		if (localName.equals("import")){
			try {
				XSDParser xsdParser = new XSDParser(this.progressMonitor);
				String fileName = attributes.getValue("schemaLocation").substring(attributes.getValue("schemaLocation").lastIndexOf("/")+1);
				xsdParser.parse(fileName, new File(ServerGUI.GLOBAL_WADL_PATH + "\\PortoSeguroRamosElementaresEOL\\EBO"), false, this.responseFiles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (localName.equals("complexType")){
			
			responseFile = new ResponseFile();	
			responseFile.setName(attributes.getValue("name"));
		}
		
		if (localName.equals("element")){
			if (responseFile != null && attributes.getValue("name") != null){
				
				Attribute attribute = new Attribute();
				
						  if (attributes.getValue("maxOccurs") != null){
							  attribute.setMaxOccurs(attributes.getValue("maxOccurs"));
						  }	
				
						  if (attributes.getValue("type")!= null && attributes.getValue("type").indexOf(":") != -1){
							  attribute.setType(attributes.getValue("type").substring(attributes.getValue("type").indexOf(":")+1));  
						  }	else {
							  attribute.setType(attributes.getValue("type"));
						  }
						  
						  if (responseFile.getMapAttributes().containsKey(attributes.getValue("type"))){
							  attribute.setContent((Map<String, Object>) ((Attribute) responseFile.getMapAttributes().get(attributes.getValue("type"))).getContent());  
						  }
				
				responseFile.getMapAttributes().put(attributes.getValue("name"), attribute);	
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if (localName.equals("element")){
			if (responseFile != null && responseFile.getName() != null){
				this.responseFiles.put(responseFile.getName().toUpperCase(), responseFile);	
			}
		}
	}
	
	
}
