package net.jewczuk.openbip.rest.objects;

public class AjaxGenericResponse {

	private boolean error;
	private String message;
	private String code;

	public AjaxGenericResponse() {

	}

	public AjaxGenericResponse(boolean error, String message, String code) {
		this.error = error;
		this.message = message;
		this.code = code;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
