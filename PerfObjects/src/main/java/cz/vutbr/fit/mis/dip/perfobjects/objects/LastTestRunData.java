package cz.vutbr.fit.mis.dip.perfobjects.objects;

public class LastTestRunData {
	private String description;
	private boolean success;
	private String error;
	private String parameters;
	
	public LastTestRunData() {
	}
	
	public LastTestRunData(String description, boolean success, String error, String parameters) {
		this.description = description;
		this.success = success;
		this.error = error;
		this.parameters = parameters;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
