package xingu.http.client;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.apache.commons.io.IOUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Ignore;
import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.http.client.impl.ApacheHttpClient;
import xingu.utils.MD5Utils;
import xingu.utils.StringUtils;
import xingu.utils.io.FileUtils;

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
	@Ignore
	public void testDynamicBuffer()
		throws Exception
	{
		int           size;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(ByteOrder.LITTLE_ENDIAN, 4 * 1024);
		byte[]        tmp    = new byte[4 * 1024];
		InputStream is = FileUtils.toInputStream("/home/leandro/xx.pdf");
		byte[] data = IOUtils.toByteArray(is);
		
		InputStream is2 = new ByteArrayInputStream(data);
		while ((size = is2.read(tmp)) != -1)
        {
        	buffer.writeBytes(tmp, 0, size);
        }
		
		int readable = buffer.readableBytes();
		byte[] result = new byte[readable];
		buffer.getBytes(0, result);
		
		assertEquals(data.length, result.length);
		for(int i = 0; i < data.length; i++)
		{
			byte b1 = data[i];
			byte b2 = result[i];
			assertEquals(b1, b2);
		}

		System.out.println(result.length);
		System.out.println(MD5Utils.md5Hash(result));
	}
	
	@Test
	@Ignore
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
