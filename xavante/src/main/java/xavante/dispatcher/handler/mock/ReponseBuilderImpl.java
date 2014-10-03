package xavante.dispatcher.handler.mock;



import xavante.XavanteRequest;
import xingu.http.client.HttpResponse;

public class ReponseBuilderImpl
	implements ReponseBuilder
{
	private String			path;

	private HttpResponse	response;

	public ReponseBuilderImpl(String path, HttpResponse response)
	{
		this.path     = path;
		this.response = response;
	}

	@Override
	public String getPath()
	{
		return path;
	}

	@Override
	public HttpResponse handle(XavanteRequest req)
		throws Exception
	{
		return response;
	}
}