package br.com.ibnetwork.xingu.utils.collection;

import java.util.Collection;

public class CollectionDifference<T>
{
	private Collection<T> inter, onlyA, onlyB;

	public CollectionDifference(Collection<T> inter, Collection<T> onlyA, Collection<T> onlyB)
	{
		this.inter = inter;
		this.onlyA = onlyA;
		this.onlyB = onlyB;
	}

	public Collection<T> intersection()
	{
		return inter;
	}

	public Collection<T> onA()
	{
		return onlyA;
	}

	public Collection<T> onB()
	{
		return onlyB;
	}

	public void forAllDo(Closure<T> closureForA, Closure<T> closureForIntersection, Closure<T> closureForB)
	{
		CollectionUtils.forAllDo(onlyA, closureForA);
		CollectionUtils.forAllDo(inter, closureForIntersection);
		CollectionUtils.forAllDo(onlyB, closureForB);
	}

	@Override
	public String toString()
	{
		return 	"a -> " + onlyA + "\n" +
				"i -> " + inter + "\n" +
				"b -> " + onlyB;
	}
}
