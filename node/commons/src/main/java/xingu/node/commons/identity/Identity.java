package xingu.node.commons.identity;

import java.io.Serializable;

public interface Identity<T>
	extends Serializable
{
	T get();
}
