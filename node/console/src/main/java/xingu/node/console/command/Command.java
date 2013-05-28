package xingu.node.console.command;

public interface Command<T>
{
	T execute(String[] args, Writer writer)
		throws Exception;
}