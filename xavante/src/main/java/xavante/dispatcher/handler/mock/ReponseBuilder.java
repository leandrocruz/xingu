package xavante.dispatcher.handler.mock;



import xavante.XavanteRequest;
import xingu.http.client.HttpResponse;
import xingu.utils.collection.FluidMap;

public interface ReponseBuilder
{
	HttpResponse handle(XavanteRequest req)
		throws Exception;

	String getPath();
	
	FluidMap<String> getParameters();
}
