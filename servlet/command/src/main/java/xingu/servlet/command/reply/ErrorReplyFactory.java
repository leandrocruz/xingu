package xingu.servlet.command.reply;

import org.apache.commons.lang3.exception.ExceptionUtils;

import xingu.servlet.command.CommandReply;

public class ErrorReplyFactory
{
	public static ErrorReply asError(Throwable t, String message)
	{
		String trace = ExceptionUtils.getStackTrace(t);
		return new ErrorReply(t, message, trace);
	}

	public static CommandReply asError(String message)
	{
		return new ErrorReply(message);
	}
}
