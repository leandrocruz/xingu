package xingu.utils.sm;

public class StateMachine<T, R>
{
	private State<T, R> start;
	
	private State<T, R> current;
	
	public StateMachine(State<T, R> start)
	{
		this.start = start;
	}
	
	public State<T, R> current()
	{
		return current != null ? current : start;
	}

	public StateMachine<T, R> on(T t)
	{
		State<T, R> state = current();
		State<T, R> next = state.on(t);
		if(next != state)
		{
			current = next;
		}
		return this;
	}
}
