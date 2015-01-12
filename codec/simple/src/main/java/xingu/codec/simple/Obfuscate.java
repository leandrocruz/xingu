package xingu.codec.simple;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import xingu.lang.NotImplementedYet;

public class Obfuscate
	implements Converter<String>
{
	@Override
	public String read(InputNode node)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public void write(OutputNode node, String value)
		throws Exception
	{
		String replacement = StringUtils.repeat("*", value.length());
		node.setValue(replacement);
	}
}
