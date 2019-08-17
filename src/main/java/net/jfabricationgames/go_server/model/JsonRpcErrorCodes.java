package net.jfabricationgames.go_server.model;

public enum JsonRpcErrorCodes {
	
	UNKNOWN_ERROR(-10000),//
	LOGIN_ERROR(-10100),//
	UNEXPECTED_PARAMETERS_ERROR(-11000),//
	UNKNOWN_METHOD_ERROR(-12000),//
	METHOD_INVOKE_ERROR(-12100),//
	EXECUTION_ERROR(-13000);//
	
	private final int code;
	
	private JsonRpcErrorCodes(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}