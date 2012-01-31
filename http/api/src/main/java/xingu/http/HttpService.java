package xingu.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

public interface HttpService
{
    HttpResponse request(String url);
    
    <T> T exec(HttpUriRequest request, ResponseHandler<T> handler)
        throws Exception;
}
