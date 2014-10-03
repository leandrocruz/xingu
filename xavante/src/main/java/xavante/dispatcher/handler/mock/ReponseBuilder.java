package xavante.dispatcher.handler.mock;



import xavante.XavanteRequest;
import xingu.http.client.HttpResponse;

public interface ReponseBuilder
{
	HttpResponse handle(XavanteRequest req)
			throws Exception;

	String getPath();
}
