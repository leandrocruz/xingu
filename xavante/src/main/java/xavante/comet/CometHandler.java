package xavante.comet;

public interface CometHandler
{
	String onMessage(CometMessage msg)
		throws Exception;

	String onError(Throwable t);
}