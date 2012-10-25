package xingu.servlet.command.reply;

import org.apache.commons.lang.exception.ExceptionUtils;

import xingu.servlet.command.CommandReply;

public class ErrorReplyFactory
{
	public static ErrorReply asError(Throwable t, String message)
	{
		String trace = ExceptionUtils.getFullStackTrace(t);
		return new ErrorReply(t, message, trace);
	}

	public static CommandReply asError(String message)
	{
		return new ErrorReply(message);
	}
}
