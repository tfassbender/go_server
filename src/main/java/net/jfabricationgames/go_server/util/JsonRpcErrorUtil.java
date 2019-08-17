package net.jfabricationgames.go_server.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.jfabricationgames.go_server.model.JsonRpcError;
import net.jfabricationgames.go_server.model.JsonRpcErrorResponse;
import net.jfabricationgames.go_server.service.GoService;

/**
 * Creates errors that are often used
 */
public abstract class JsonRpcErrorUtil {
	
	/**
	 * Create a default error response (HTTP code is 200, but the returned JsonRpcResponse contains a JsonRpcError with an error-code of -10000)
	 */
	public static Response createErrorResponse(String id) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(-10000, "Unknown error occured", null));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create a response that informs about problems on login request
	 */
	public static Response createLoginErrorResponse(String id) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(-10100, "Login was not successful", null));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create a response that informs about illegal or unexpected parameters in a request
	 */
	public static Response createIllegalParameterErrorResponse(String id, Object parameters) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(-11000, "Unexpected parameters in request", parameters));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create a response that informs that the requested method (the method parameter in the request) is unknown
	 */
	public static Response createMethodNotFoundErrorResponse(String id, String methodName) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(-12000, "Unknown method", methodName));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create an empty response with only an id and the default jsonRpc fields set
	 */
	private static JsonRpcErrorResponse createEmptyErrorResponse(String id) {
		JsonRpcErrorResponse response = new JsonRpcErrorResponse();
		response.setId(id);
		response.setJsonRpc(GoService.JSON_RPC);
		return response;
	}
}