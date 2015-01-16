package xingu.http.client;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.http.client.impl.ApacheHttpClient;
import xingu.utils.MD5Utils;
import xingu.utils.StringUtils;

public class DownloadProgressTest
	extends XinguTestCase
{
	@Inject
	private HttpClient http;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(HttpClient.class).to(ApacheHttpClient.class);
	}

	@Test
	public void testDownload()
		throws Exception
	{
		HttpProgressListener listener = new HttpProgressListener()
		{
			@Override
			public void on(long total, long progress)
				throws Exception
			{
				System.out.println(progress + "/" + total + " (" + StringUtils.toPercent(progress, total) + ")");				
			}
		};
		
		String       uri   = "http://repo-g.oystr.com.br/test/sample.pdf";
		HttpResponse res   = http.get(uri).listener(listener).exec();
		InputStream  is    = res.getRawBody();
		byte[]       bytes = IOUtils.toByteArray(is);
		String       hash  = MD5Utils.md5Hash(bytes);
		assertEquals("83502a48f31d120f8051379bd42a45ea", hash);
		
//		File         tmp = File.createTempFile("sample-", ".pdf");
//		OutputStream os  = new FileOutputStream(tmp);
//		IOUtils.copy(is, os);
//		IOUtils.closeQuietly(os);
//		System.out.println(tmp);
	}
}
