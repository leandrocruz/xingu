package xingu.codec.simple;

import java.util.Map;

import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import xingu.lang.NotImplementedYet;

public class SuppressClassAttributeStrategy
	implements Strategy
{
	@Override
	public Value read(Type type, NodeMap<InputNode> node, Map map)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean write(Type type, Object value, NodeMap<OutputNode> node, Map map)
		throws Exception
	{
		return false;
	}

}
