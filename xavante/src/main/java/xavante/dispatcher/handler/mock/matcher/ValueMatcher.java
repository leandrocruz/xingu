package xavante.dispatcher.handler.mock.matcher;

import java.util.List;

public interface ValueMatcher
{
	boolean matchAny(List<String> values);
}
