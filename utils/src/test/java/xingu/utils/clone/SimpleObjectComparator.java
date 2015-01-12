package xingu.utils.clone;

import java.util.Comparator;

public class SimpleObjectComparator
	implements Comparator<SimpleObject>
{

	@Override
	public int compare(SimpleObject o1, SimpleObject o2)
	{
		return o1.i() - o2.i();
	}

}
