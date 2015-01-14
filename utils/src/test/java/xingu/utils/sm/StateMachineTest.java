package xingu.utils.sm;

import static org.junit.Assert.*;

import org.junit.Test;

public class StateMachineTest
{
	@Test
	public void testStateMachine()
	{
		State<Character, Void> s     = new SimpleState<Character, Void>("s");
		State<Character, Void> e     = new SimpleState<Character, Void>("e");
		State<Character, Void> y     = new SimpleState<Character, Void>("y");
		State<Character, Void> start = new SimpleState<Character, Void>("start");
		
		start.when('y', y);
		y.when('e', e);
		e.when('s', s);
		
		State<Character, Void> state = null;

		StateMachine<Character, Void> sm = new StateMachine<>(start);
		
		sm.on('a');
		state = sm.current();
		assertSame(start, state);

		sm.on('y');
		state = sm.current();
		assertSame(y, state);

		sm.on('y');
		state = sm.current();
		assertSame(y, state);

		sm.on('e');
		state = sm.current();
		assertSame(e, state);

		sm.on('s');
		state = sm.current();
		assertSame(s, state);
		
		sm.on('y');
		state = sm.current();
		assertSame(s, state);
	}
}