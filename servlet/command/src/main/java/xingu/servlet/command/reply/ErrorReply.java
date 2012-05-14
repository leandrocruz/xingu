package xingu.servlet.command.reply;

import org.apache.commons.lang.exception.ExceptionUtils;

import xingu.servlet.command.CommandReplySupport;

public class ErrorReply
    extends CommandReplySupport
{
	private String errorClass;
	
	private String errorMessage;
	
	private final String message;

	private String stackTrace;
	

	public ErrorReply(Throwable t, String message)
	{
		this.message = message;
		this.errorClass = t.getClass().getName();
		this.errorMessage = t.getMessage();
		this.stackTrace = ExceptionUtils.getFullStackTrace(t);
	}


	public String getMessage() {return message;}
	public String getErrorClass() {return errorClass;}
	public String getErrorMessage() {return errorMessage;}
	public String getStackTrace() {return stackTrace;}
	
}
