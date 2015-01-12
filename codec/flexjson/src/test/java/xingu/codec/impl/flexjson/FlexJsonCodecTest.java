package xingu.codec.impl.flexjson;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.Map;

import org.junit.Test;

import xingu.codec.Codec;
import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.lang.NotImplementedYet;
import xingu.utils.classloader.ClassLoaderManager;
import xingu.utils.classloader.NamedClassLoader;

public class FlexJsonCodecTest
	extends XinguTestCase
{
	@Inject
	private Codec codec;

	@Inject
	private ClassLoaderManager clm;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(Codec.class).to(FlexJsonCodec.class);
		withMock(ClassLoaderManager.class);
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testEmptyJsonObject()
		throws Exception
	{
		String input = "{}";
		Object result = codec.decode(input);
		assertTrue(result instanceof Map);
		
		Map map = (Map) result;
		assertTrue(map.isEmpty());
	}
	
	@Test
	public void testUserDefinedObject()
		throws Exception
	{
		String input = "{class: \"xingu.codec.impl.flexjson.SampleBean\", f1: 10}";
		Object result = codec.decode(input);
		assertTrue(result instanceof SampleBean);
	
		SampleBean bean = (SampleBean) result;
		assertEquals(10, bean.getF1());
		assertEquals(null, bean.getF2());

		input = "{class: \"xingu.codec.impl.flexjson.SampleBean\", f1: 10, f2: 99}";
		bean = (SampleBean) codec.decode(input);
		assertEquals(10, bean.getF1());
		assertEquals(new Integer(99), bean.getF2());
	}
	
	@Test
	public void testNamedClassLoader()
		throws Exception
	{
		NamedClassLoader loader = new NamedClassLoader()
		{
			
			@Override
			public Class<?> loadClass(String name)
				throws Exception
			{
				if("SampleBean".equals(name))
					return SampleBean.class;
				
				throw new NotImplementedYet();
			}
			
			@Override
			public String id()
			{
				return null;
			}
			
			@Override
			public URL getResource(String string)
			{
				return null;
			}
			
			@Override
			public ClassLoader getClassLoader()
			{
				return null;
			}
		};
		
		when(clm.byId("x")).thenReturn(loader);
		String input = "{class: \"SampleBean\", classLoader: \"x\", f1: 10}";
		Object result = codec.decode(input);
		assertTrue(result instanceof SampleBean);

	}
}
