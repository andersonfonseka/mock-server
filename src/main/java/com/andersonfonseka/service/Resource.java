package com.andersonfonseka.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.andersonfonseka.domain.ForwardPayload;
import com.andersonfonseka.domain.Operation;
import com.andersonfonseka.domain.ResponseFile;
import com.andersonfonseka.domain.Service;
import com.andersonfonseka.gui.ServerGUI;
import com.andersonfonseka.monitor.Message;
import com.andersonfonseka.monitor.ProgressMonitor;

@Path("")
public class Resource {

	private static ProgressMonitor progressMonitor;

	private static Map<String, Service> services = new HashMap<String, Service>();;

	public Resource() {
	}

	public static void setProgressMonitor(ProgressMonitor progressMonitor) {
		Resource.progressMonitor = progressMonitor;
	}

	public static void setServices(Map<String, Service> services) {
		Resource.services.putAll(services);
	}

	@GET
	@Path("/{params: .*}")
	@Produces(MediaType.APPLICATION_JSON)
	public javax.ws.rs.core.Response get(@Context HttpHeaders headers, @Context UriInfo ui) {
		
		ForwardPayload payload = getForwardComposition(headers, ui, "GET");

		progressMonitor.addMessage(new Message(Message.GENERIC, payload.getUri() + "/GET"));
		progressMonitor.addMessage(new Message(Message.GET, payload.getUri() + "/GET"));

		Executor executor = new Executor(payload.getUri(), "GET") {

			@Override
			String perform(String params, String data, Operation operation) {
				return operation.getResponse(params, "GET");
			}
		};

		executor.setData(payload.getForwardResult());

		String result = executeOperation(executor);

		return javax.ws.rs.core.Response.ok(result).build();
	}

	@POST
	@Path("/{params: .*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public javax.ws.rs.core.Response post(@Context HttpHeaders headers, String data, @Context UriInfo ui) {

		ForwardPayload payload = getForwardComposition(headers, ui, "POST", data);

		progressMonitor.addMessage(new Message(Message.GENERIC, payload.getUri() + "/POST"));
		progressMonitor.addMessage(new Message(Message.POST, payload.getUri() + "/POST"));

		Executor executor = new Executor(payload.getUri(), "POST") {

			@Override
			String perform(String params, String data, Operation operation) {
				return operation.getResponse(params, "POST");
			}
		};

		executor.setData(payload.getForwardResult());

		String result = executeOperation(executor);

		return javax.ws.rs.core.Response.ok(result).build();
	}

	@PUT
	@Path("/{params: .*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public javax.ws.rs.core.Response put(@Context HttpHeaders headers, String data, @Context UriInfo ui) {

		ForwardPayload payload = getForwardComposition(headers, ui, "PUT", data);

		progressMonitor.addMessage(new Message(Message.GENERIC, payload.getUri() + "/PUT"));
		progressMonitor.addMessage(new Message(Message.PUT, payload.getUri() + "/PUT"));

		Executor executor = new Executor(payload.getUri(), "PUT") {

			@Override
			String perform(String params, String data, Operation operation) {
				return operation.getResponse(params, "PUT");
			}
		};

		executor.setData(payload.getForwardResult());

		String result = executeOperation(executor);

		return javax.ws.rs.core.Response.ok(result).build();
	}

	@DELETE
	@Path("/{params: .*}")
	@Produces(MediaType.APPLICATION_JSON)
	public javax.ws.rs.core.Response delete(@Context HttpHeaders headers, String data, @Context UriInfo ui) {

		ForwardPayload payload = getForwardComposition(headers, ui, "DELETE", data);

		progressMonitor.addMessage(new Message(Message.GENERIC, payload.getUri() + "/DELETE"));
		progressMonitor.addMessage(new Message(Message.DELETE, payload.getUri() + "/DELETE"));

		Executor executor = new Executor(payload.getUri(), "DELETE") {

			@Override
			String perform(String params, String data, Operation operation) {
				return operation.getResponse(params, "DELETE");
			}
		};

		executor.setData(payload.getForwardResult());

		String result = executeOperation(executor);

		return javax.ws.rs.core.Response.ok(result).build();
	}
	
	private String checkForwardMode(HttpHeaders headers, String path, String method, String... payload) {
		String forwardResult = null;

		try {
			if (ServerGUI.GLOBAL_FORWARD_MODE) {
				forwardResult = new Forward(progressMonitor).result(headers, ServerGUI.GLOBAL_REAL_HOST + path, method,
						payload);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			forwardResult = "";
		}
		return forwardResult;
	}

	private String executeOperation(Executor executor) {

		Service service = new Service();
			
		service.setUrl(executor.getParams());

		Operation operation = new Operation();
		operation.setName(executor.getMethod());

		ResponseFile responseFile = new ResponseFile();
		responseFile.setName("response");
		responseFile.setPayload(executor.getData());

		operation.setResponse(responseFile);
		service.addOperation(operation);

		List<Operation> operations = service.getOperations();

		if (!operations.isEmpty()) {
			for (Operation op : operations) {

				if (op.getName().equals(executor.getMethod())) {
					return executor.perform(service.getUrl(), executor.getData(), op);
				}
			}
		}

		return "";
	}

	private ForwardPayload getForwardComposition(HttpHeaders httpHeaders, UriInfo ui, String method, String... data){
		
		ForwardPayload payload = new ForwardPayload();
		
		if (ui.getRequestUri().getQuery() != null && ui.getRequestUri().getQuery().length() > 0){
			payload.setUri(ui.getPath() + "?" + ui.getRequestUri().getQuery());
			payload.setForwardResult(checkForwardMode(httpHeaders, payload.getUri(), method, data));
		} else {
			payload.setUri(ui.getPath());
			payload.setForwardResult(checkForwardMode(httpHeaders, payload.getUri(), method, data));
		}
		
		return payload;
	}
	
}
