package dh.hongyi.excel.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 业务异常类
 */
public class GenericBusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4264030374230852982L;

	private String errorCode = "0";
	
	private String errorDescription = "";
	
	private String errorDetail = "";

	public GenericBusinessException() {
	}

	/**
	 * 不要轻易使用
	 * 
	 * @param cause
	 */
	public GenericBusinessException(Throwable cause) {
		super(cause);
	}

	public GenericBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenericBusinessException(Throwable cause, String errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public void printStackTrace() {
		System.err.println(this.getErrorCode());
		System.err.println(this.getMessage());
		super.printStackTrace();
	}

	public void printStackTrace(PrintStream s) {
		s.println(this.getErrorCode());
		s.println(this.getMessage());
		super.printStackTrace(s);
	}

	public void printStackTrace(PrintWriter s) {
		s.println(this.getErrorCode());
		s.println(this.getMessage());
		super.printStackTrace(s);
	}

	public GenericBusinessException(String errorDescription) {
		super(errorDescription);
		this.errorDescription = errorDescription;
	}

	public GenericBusinessException(String errorDescription, String errorDetail) {
		super(errorDescription);
		this.errorDescription = errorDescription;
		this.errorDetail = errorDetail;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
}
