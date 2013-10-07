package xavante.comet;

public interface Registry
{
	boolean has(String id);

	CometClient newClient();

	CometClient byId(String id);
}
