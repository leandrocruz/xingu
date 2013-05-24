package xingu.node.console.command;

public interface Command
{
	Object execute(String[] args)
		throws Exception;
}