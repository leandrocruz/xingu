package xingu.http;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.jboss.netty.handler.codec.http.HttpHeaders;

import xingu.container.Container;
import xingu.container.ContainerUtils;
import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.http.client.Attachment;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.AttachmentImpl;
import xingu.utils.io.FileUtils;

public class CurlRunner
{
	@Inject
	private HttpClient http;

	public static void main(String[] args)
		throws Exception
	{
		InputStream is        = FileUtils.toInputStream("pulga-curl.xml");
		Container   container = ContainerUtils.getContainer(is);
		Factory     factory   = container.lookup(Factory.class);
		CurlRunner  runner    = factory.create(CurlRunner.class);
		runner.call();
	}

	private void call()
		throws Exception
	{
		URL        file       = Thread.currentThread().getContextClassLoader().getResource("pulga-curl.xml");
		Attachment attachment = new AttachmentImpl("curriculum", new File(file.getFile()));
		HttpResponse res = http
				.post("localhost:8080")
				.header(HttpHeaders.Names.CONTENT_TYPE, "multipart/form-data; charset=ISO-8859-1")
				//.header(HttpHeaders.Names.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=ISO-8859-1")
				.multipart(true)
				.withAttachment(attachment)
				.field("name", "Léandro Cabra Macho!")
				.exec();
	
		String body = res.getBody("utf8");
		System.out.println(body);
	}
}
