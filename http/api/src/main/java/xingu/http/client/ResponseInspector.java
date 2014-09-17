package xingu.http.client;

public interface ResponseInspector
{
	void throwErrorIf(HttpResponse res)
		throws Exception;
}
