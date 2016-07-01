package xingu.codec.simple;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.transform.RegistryMatcher;

import xingu.codec.Codec;
import xingu.codec.impl.CodecSupport;
import xingu.lang.NotImplementedYet;

public class SimpleCodec
	extends CodecSupport
	implements Codec, Initializable
{
	private Serializer	serializer;

	private boolean suppressClassAttribute;

	private String format;

	private String timeZone;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		suppressClassAttribute = conf.getChild("classAttribute").getAttributeAsBoolean("suppress", true);
		Configuration df = conf.getChild("dateFormat");
		this.format      = df.getAttribute("format", "yyyy-MM-dd HH:mm:ss.S ZZZ");
		this.timeZone    = df.getAttribute("timezone", "UTC");
		super.configure(conf);
	}

	@Override
	public void initialize()
		throws Exception
	{
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		RegistryMatcher matchers = new RegistryMatcher();
		matchers.bind(java.util.Date.class, new DateTransformer(df));
		Strategy strategy = suppressClassAttribute ? new SuppressClassAttributeStrategy() : new TreeStrategy();
		serializer = new Persister(new AnnotationStrategy(strategy), matchers);
	}

	@Override
	public Object decode(String text)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public <T> T decode(String text, Class<? extends T> clazz)
		throws Exception
	{
		return serializer.read(clazz, text);
	}

	@Override
	public String encode(Object object)
		throws Exception
	{
		StringWriter writer = new StringWriter();
		serializer.write(object, writer);
		return writer.toString();
	}
}