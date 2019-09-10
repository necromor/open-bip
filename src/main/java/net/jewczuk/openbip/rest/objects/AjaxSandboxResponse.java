package net.jewczuk.openbip.rest.objects;

public class AjaxSandboxResponse extends AjaxGenericResponse {

	private String content;

	public AjaxSandboxResponse() {

	}

	public AjaxSandboxResponse(boolean error, String message, String content) {
		super(error, message);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
