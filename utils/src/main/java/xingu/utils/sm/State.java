package xingu.utils.sm;

public interface State<T, R>
{
	String name();

	State<T, R> on(T value);

	void when(T value, State<T, R> next);
	
	void when(T value, State<T, R> next, State<T, R> fallback);

	void any(State<T, R> next);
	
	void appendResult(R result);

	void append(T value);

	R collect();

	State<T, R> end();
}
