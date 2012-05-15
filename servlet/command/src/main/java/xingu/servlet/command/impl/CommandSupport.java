package xingu.servlet.command.impl;

import xingu.servlet.command.Command;

public class CommandSupport
	implements Command
{
	private long id;
	
	private String stamp;
	
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	@Override public String getChannel() {return stamp;}
	@Override public void setChannel(String stamp) {this.stamp = stamp;}
}
