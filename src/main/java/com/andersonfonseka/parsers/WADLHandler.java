package com.andersonfonseka.parsers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.andersonfonseka.domain.Attribute;
import com.andersonfonseka.domain.Operation;
import com.andersonfonseka.domain.Param;
import com.andersonfonseka.domain.Request;
import com.andersonfonseka.domain.ResponseFile;
import com.andersonfonseka.domain.Service;
import com.andersonfonseka.generators.DataGeneratorFactory;
import com.andersonfonseka.monitor.Message;
import com.andersonfonseka.monitor.ProgressMonitor;

public class WADLHandler extends DefaultHandler {
	
	private Map<String, Integer> typesMap = new HashMap<String, Integer>();

	private ProgressMonitor progressMonitor;

	private Map<Integer, String> levels = new HashMap<Integer, String>();
	
	private Map<String, String> schemaElements = new HashMap<String, String>();

	private Map<String, Service> services;

	private int level = 0;

	private Operation operation;

	private Request request;
	
	private boolean responseStatus = false;
	
	private boolean schemaStatus = false;
	
	private File fileWADL;
	
	private Map<String, ResponseFile> responseFiles = new HashMap<String, ResponseFile>();

	public WADLHandler(ProgressMonitor progressMonitor, Map<String, Service> services, File file) {
		super();
		this.services = services;
		this.progressMonitor = progressMonitor;
		this.fileWADL = file;
		
		typesMap.put("string",1);
		typesMap.put("integer",2);
		typesMap.put("long",3);
		typesMap.put("short",4);
		typesMap.put("decimal",5);
		typesMap.put("float",6);
		typesMap.put("date",7);
		typesMap.put("dateTime",8);
		typesMap.put("positiveInteger",9);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (localName.equals("import")) {

			XSDParser xsdParser = new XSDParser(this.progressMonitor);
			try {
				xsdParser.parse(attributes.getValue("schemaLocation"), fileWADL, true, this.responseFiles);
				responseFiles.putAll(xsdParser.getResponseFiles());
				
				Iterator<String> it = responseFiles.keySet().iterator();
				
				while(it.hasNext()){
					
					String key = it.next();
					
					ResponseFile responseFile = responseFiles.get(key);
					
					Iterator<String> itKeyAttrib = responseFile.getMapAttributes().keySet().iterator();
					
					while(itKeyAttrib.hasNext()){
						
						String keyAttrib = itKeyAttrib.next();
						
						if (responseFile.getMapAttributes().get(keyAttrib) instanceof Attribute){

							Attribute attribute = (Attribute) responseFile.getMapAttributes().get(keyAttrib);
							
							if (attribute.getType() != null && responseFiles.containsKey(attribute.getType().toUpperCase())){
								attribute.setContent(responseFiles.get(attribute.getType().toUpperCase()).getMapAttributes());
							} else {
								responseFile.getMapAttributes().put(keyAttrib, DataGeneratorFactory.getInstance().getGenerator(getTypes(attribute.getType())).generate());
							}
						}
						
					}
				}
				
			} catch (ParserConfigurationException e1) {
				progressMonitor.addMessage(new Message(Message.GENERIC, e1.getMessage()));
			} catch (SAXException e1) {
				progressMonitor.addMessage(new Message(Message.GENERIC, e1.getMessage()));
			} catch (IOException e1) {
				progressMonitor.addMessage(new Message(Message.GENERIC, e1.getMessage()));
			}

		}
		
		if (localName.equals("schema")) {
			schemaStatus = true;
		}
		
		if (localName.equals("element") && schemaStatus){
			schemaElements.put(attributes.getValue("name"), attributes.getValue("type").substring(4).toUpperCase());
		}

		if (localName.equals("resources")) {

			progressMonitor.addMessage(new Message(Message.GENERIC, 
					"Parsing resources..." + attributes.getValue("base").substring(attributes.getValue("base").indexOf("8080") + 4)));

			levels.put(level, attributes.getValue("base").substring(attributes.getValue("base").indexOf("8080") + 4));
			level++;
		}

		if (localName.equals("resource")) {

			String path = attributes.getValue("path");

			this.progressMonitor.addMessage(new Message(Message.GENERIC, "Parsing resource..." + path));

			if (path.indexOf("/") == -1) {
				levels.put(level, "/" + path);
			} else {
				levels.put(level, path);
			}

			Service service = new Service();

			if (path.indexOf("/") == -1) {
				service.setUrl(getPath() + "/" + attributes.getValue("path"));
				services.put(getPath() + "/" + attributes.getValue("path"), service);
			} else {
				service.setUrl(getPath() + attributes.getValue("path"));
				services.put(getPath() + attributes.getValue("path"), service);
			}

			level++;
		}

		if (localName.equals("method")) {

			Service serv = services.get(getPath());

			operation = new Operation();
			operation.setId(attributes.getValue("id"));
			operation.setName(attributes.getValue("name"));

			serv.addOperation(operation);
		}

		if (localName.equals("request")) {
			request = new Request();
			operation.setRequest(request);
		}
		
		if (localName.equals("response")) {
			if (attributes.getValue("status") != null && attributes.getValue("status").equals("200")){
				responseStatus = true;
			}
		}
		
		
		if (localName.equals("representation") && responseStatus && attributes.getValue("element") != null) {
			
			int position = attributes.getValue("element").indexOf(":");
			String elementName = attributes.getValue("element").substring(position+1);
			operation.setResponse(responseFiles.get(schemaElements.get(elementName)));	
			
			responseStatus = false;
		}

		if (localName.equals("param")) {
			Param param = new Param();

			param.setName(attributes.getValue("name"));
			param.setType(attributes.getValue("type"));

			if (request != null) {
				request.addParam(param);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("resource")) {
			level--;
		}
		
		if (localName.equals("schema")) {
			schemaStatus = false;
		}
	}

	private String getPath() {

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < level; i++) {
			builder.append(levels.get(i));
		}

		return builder.toString();
	}

	public int getTypes(String typeKey){
		
		if (typesMap.containsKey(typeKey)){
			return typesMap.get(typeKey);	
		}
		
		return 0;
	}
	
}
