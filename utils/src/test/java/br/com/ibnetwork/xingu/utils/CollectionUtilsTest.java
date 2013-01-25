package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.utils.collection.CollectionDifference;
import br.com.ibnetwork.xingu.utils.collection.CollectionUtils;

import org.junit.Test;


public class CollectionUtilsTest
{
	@Test
	public void testAsymmetricDifferenceDisjointCollection()
		throws Exception
	{
		List<Integer> a = ArrayUtils.toList(1, 2, 3);
		List<Integer> b = ArrayUtils.toList(4, 5, 6);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, b);
		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(3, onA.size());
		assertEquals(new Integer(1), onA.get(0));
		assertEquals(new Integer(2), onA.get(1));
		assertEquals(new Integer(3), onA.get(2));

		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(3, onB.size());
		assertEquals(new Integer(4), onB.get(0));
		assertEquals(new Integer(5), onB.get(1));
		assertEquals(new Integer(6), onB.get(2));

		assertEquals(0, diff.intersection().size());
	}

	@Test
	public void testAsymmetricDifferenceWithIntersection()
		throws Exception
	{
		List<Integer> a = ArrayUtils.toList(1, 2, 3);
		List<Integer> b = ArrayUtils.toList(2, 3, 4);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, b);
		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(1, onA.size());
		assertEquals(new Integer(1), onA.get(0));

		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(1, onB.size());
		assertEquals(new Integer(4), onB.get(0));

		List<Integer> intersection = (List<Integer>) diff.intersection();
		assertEquals(2, intersection.size());
		assertEquals(new Integer(2), intersection.get(0));
		assertEquals(new Integer(3), intersection.get(1));
	}

	@Test
	public void testAsymmetricDifferenceWithIntersectionForAllElementsOnA()
		throws Exception
	{
		List<Integer> a = ArrayUtils.toList(2, 3);
		List<Integer> b = ArrayUtils.toList(2, 3, 4);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, b);
		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(0, onA.size());

		List<Integer> intersection = (List<Integer>) diff.intersection();
		assertEquals(2, intersection.size());
		assertEquals(new Integer(2), intersection.get(0));
		assertEquals(new Integer(3), intersection.get(1));

		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(1, onB.size());
		assertEquals(new Integer(4), onB.get(0));
	}

	@Test
	public void testAsymmetricDifferenceWithIntersectionForAllElementsOnB()
		throws Exception
	{
		List<Integer> a = ArrayUtils.toList(1, 2, 3);
		List<Integer> b = ArrayUtils.toList(2, 3);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, b);
		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(1, onA.size());
		assertEquals(new Integer(1), onA.get(0));

		List<Integer> intersection = (List<Integer>) diff.intersection();
		assertEquals(2, intersection.size());
		assertEquals(new Integer(2), intersection.get(0));
		assertEquals(new Integer(3), intersection.get(1));

		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(0, onB.size());
	}

	@Test
	public void testAsymmetricDifferenceWithIntersectionForAllElements()
		throws Exception
	{
		List<Integer> a = ArrayUtils.toList(1, 2, 3);
		List<Integer> b = ArrayUtils.toList(1, 2, 3);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, b);
		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(0, onA.size());

		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(0, onB.size());

		List<Integer> intersection = (List<Integer>) diff.intersection();
		assertEquals(3, intersection.size());
		assertEquals(new Integer(1), intersection.get(0));
		assertEquals(new Integer(2), intersection.get(1));
		assertEquals(new Integer(3), intersection.get(2));
	}

	@Test
	public void testAsymmetricDifferenceWithEmptyA()
		throws Exception
	{
		List<Integer> a = new ArrayList<Integer>();
		List<Integer> b = ArrayUtils.toList(1, 2, 3);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, b);
		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(0, onA.size());

		assertEquals(null, diff.intersection());

		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(3, onB.size());

		assertEquals(new Integer(1), onB.get(0));
		assertEquals(new Integer(2), onB.get(1));
		assertEquals(new Integer(3), onB.get(2));
	}

	@Test
	public void testAsymmetricDifferenceWithNullA()
		throws Exception
	{
		List<Integer> b = ArrayUtils.toList(1, 2, 3);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(null, b);
		assertEquals(null, diff.onA());
		assertEquals(null, diff.intersection());

		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(3, onB.size());

		assertEquals(new Integer(1), onB.get(0));
		assertEquals(new Integer(2), onB.get(1));
		assertEquals(new Integer(3), onB.get(2));
	}

	@Test
	public void testAsymmetricDifferenceWithNullB()
		throws Exception
	{
		List<Integer> a = ArrayUtils.toList(1, 2, 3);

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, null);
		assertEquals(null, diff.onB());
		assertEquals(null, diff.intersection());

		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(3, onA.size());

		assertEquals(new Integer(1), onA.get(0));
		assertEquals(new Integer(2), onA.get(1));
		assertEquals(new Integer(3), onA.get(2));
	}

	@Test
	public void testAsymmetricDifferenceWithEmptyB()
		throws Exception
	{
		List<Integer> a = ArrayUtils.toList(1, 2, 3);
		List<Integer> b = new ArrayList<Integer>();

		CollectionDifference<Integer> diff = CollectionUtils.asymmetricDifference(a, b);
		List<Integer> onB = (List<Integer>) diff.onB();
		assertEquals(0, onB.size());

		assertEquals(null, diff.intersection());

		List<Integer> onA = (List<Integer>) diff.onA();
		assertEquals(3, onA.size());

		assertEquals(new Integer(1), onA.get(0));
		assertEquals(new Integer(2), onA.get(1));
		assertEquals(new Integer(3), onA.get(2));
	}
}
