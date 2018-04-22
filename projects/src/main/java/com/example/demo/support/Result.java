package com.example.demo.support;

public class Result<T> {
	public Header header;
	public T body;
	
	public static <T> Result<T> errorResult(String message) {
		return new Result<T>(null, message);
	}
	
	public static <T> Result<T> errorResult(String errorCode, String message) {
		return new Result<T>(errorCode, message);
	}

	public static <T> Result<T> errorResult(String errorCode, String message,
			Integer status) {
		return new Result<T>(errorCode, message, status);
	}

	public static <T> Result<T> successResult(String message, T body) {
		return new Result<T>(message, body);
	}

	public static <T> Result<T> successResult(String message, T body,
			boolean success) {
		return new Result<T>(message, body, success);
	}

	public Result() {
		super();
	}

	public Result(String message, T body) {
		this.body = body;
		this.header = new Header(message, true);
	}

	public Result(String message, boolean success) {
		this.header = new Header(message, success);
	}

	public Result(String message, T body, boolean success) {
		this.body = body;
		this.header = new Header(message, success);
	}

	public Result(String errorCode, String message) {
		this.header = new Header(errorCode, message);
	}

	public Result(String errorCode, String message, Integer status) {
		this.header = new Header(errorCode, message, false, status);
	}

	public Result(String message, boolean success, Integer status) {
		this.header = new Header(message, success, status);
	}
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}


	public class Header {
		private String errorCode;
		private String message;
		private boolean success;
		private Integer status;

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Header(String message, boolean success) {
			this.message = message;
			this.success = success;
		}

		public Header(String errorCode, String message) {
			this.errorCode = errorCode;
			this.message = message;
		}

		public Header(String message, boolean success, Integer status) {
			super();
			this.message = message;
			this.success = success;
			this.status = status;
		}

		public Header(String errorCode, String message, boolean success,
				Integer status) {
			super();
			this.errorCode = errorCode;
			this.message = message;
			this.success = success;
			this.status = status;
		}

	}

}
