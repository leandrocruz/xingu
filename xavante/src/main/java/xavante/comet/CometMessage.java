package xavante.comet;

public interface CometMessage
{
	String getToken();

	String getSequence();

	String getCommand();

	String getData();
}