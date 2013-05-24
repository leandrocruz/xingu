package xingu.node.console;

import xingu.node.console.command.Command;

public interface RemoteConsoleRegistry
{
	Command by(String name);
}
