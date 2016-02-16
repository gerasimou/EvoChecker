package pse;

final public class Output<T>
{
	public Output() { this(null); }
	public Output(T x)
	{
		this.value = x;
	}

	public T value;
}
