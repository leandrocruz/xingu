package xingu.node.console.command;

public interface Command<T>
{
	T execute(String[] args)
		throws Exception;
}