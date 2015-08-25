package xingu.http.client;

public interface HttpStateHandler
{
	Cookies getCookies();
	void setCookies(Cookies cookies);

	HttpContext getContext();
	
	HttpResponse handle(HttpRequest req, HttpResponse res)
		throws Exception;
}