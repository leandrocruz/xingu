package xingu.http.client;

public interface HttpStateKeeper
{
	Cookies getCookies();
	void setCookies(Cookies cookies);

	HttpContext getContext();
}