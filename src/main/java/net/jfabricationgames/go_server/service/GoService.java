package net.jfabricationgames.go_server.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.jfabricationgames.go_server.model.JsonRpcRequest;
import net.jfabricationgames.go_server.util.JsonRpcErrorUtil;

@Path("/go_server")
public class GoService {
	
	public static final String JSON_RPC = "2.0";
	
	private static final Logger LOGGER = LogManager.getLogger(GoService.class);
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response processRequestSynchrone(JsonRpcRequest request) {
		LOGGER.info("receive request: {}", request);
		
		try {
			//run the request asynchrone
			return CompletableFuture.supplyAsync(() -> processRequest(request)).get();
		}
		catch (InterruptedException | ExecutionException e) {
			LOGGER.warn("an execution error occured while executing the request: {}", request.getMethod());
			return JsonRpcErrorUtil.createExecutionErrorResponse(request.getId(), request.getMethod());
		}
	}
	
	/**
	 * Process any request by delegating it to the method that is to be called.
	 * 
	 * @return Returns a {@link Response} to the processed request.
	 */
	private Response processRequest(JsonRpcRequest request) {
		//execute the requested method via reflection
		try {
			GoServiceProvider provider = new GoServiceProvider();
			Class<?> clazz = provider.getClass();
			Method method = clazz.getMethod(request.getMethod(), Object.class);
			Object obj = method.invoke(provider, request.getParams());
			
			//build the response and send it back to the client
			Response response = Response.status(Status.OK).entity(obj).build();
			return response;
		}
		catch (NoSuchMethodException | SecurityException e) {
			LOGGER.warn("the request could not be processed, because the method name is unknown: {}", request.getMethod());
			return JsonRpcErrorUtil.createMethodNotFoundErrorResponse(request.getId(), request.getMethod());
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.warn("the request could not be processed, because the method could not be invoced: {}", request.getMethod());
			return JsonRpcErrorUtil.createMethodCouldNotBeInvocedErrorResponse(request.getId(), request.getMethod());
		}
	}
}