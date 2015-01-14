package xingu.utils.sm;

public interface State<T, R>
{
	String name();

	State<T, R> on(T value);

	State<T, R> when(T value, State<T, R> next);
	
	State<T, R> fallback(State<T, R> next);

	State<T, R> end();

	void append(T value);

	void appendResult(R result);

	R collect();
}