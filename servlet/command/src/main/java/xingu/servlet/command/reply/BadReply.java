package xingu.servlet.command.reply;

import xingu.servlet.command.CommandReplySupport;

public class BadReply
	extends CommandReplySupport
{
	private final String message;

	public BadReply(String message)
	{
		setOk(false);
		this.message = message;
	}

	public String getReason()
	{
		return message;
	}
}