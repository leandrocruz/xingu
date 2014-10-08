package xavante.dispatcher.handler.mock;



import br.com.ibnetwork.xingu.utils.collection.FluidMap;
import xavante.XavanteRequest;
import xingu.http.client.HttpResponse;

public interface ReponseBuilder
{
	HttpResponse handle(XavanteRequest req)
		throws Exception;

	String getPath();
	
	FluidMap<String> getParameters();
}
