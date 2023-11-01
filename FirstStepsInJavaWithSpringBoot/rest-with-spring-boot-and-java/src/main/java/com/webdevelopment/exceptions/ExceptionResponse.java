package com.webdevelopment.exceptions;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date timestamp;
	private String msg;
	private String details;

	public ExceptionResponse(Date timestamp, String msg, String details) {
		this.timestamp = timestamp;
		this.msg = msg;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMsg() {
		return msg;
	}

	public String getDetails() {
		return details;
	}

}
