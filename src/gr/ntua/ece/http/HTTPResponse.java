package gr.ntua.ece.http;

public class HTTPResponse {
	
	private int code;
	private String message;
	private String body;
	
	public HTTPResponse(int code, String message, String body) {
		this.code = code;
		this.message = message;
		this.body = body;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getBody() {
		return body;
	}
	
}