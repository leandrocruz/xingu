package xingu.utils.network;

public class PingResult
{

	private int	transmitted;

	private int	received;

	private int	loss;

	private int	time;

	private int	errors;

	public PingResult(int transmitted, int received, int loss, int time, int errors)
	{
		this.transmitted = transmitted;
		this.received    = received;
		this.loss        = loss;
		this.time        = time;
		this.errors      = errors;
	}

	public int getTransmitted()
	{
		return transmitted;
	}

	public int getReceived()
	{
		return received;
	}

	public int getLoss()
	{
		return loss;
	}

	public int getTime()
	{
		return time;
	}

	public int getErrors()
	{
		return errors;
	}

	public boolean isAlive()
	{
		return received > 0;
	}
	
	@Override
	public String toString()
	{
		String err = "";
		if(errors > 0)
		{
			err = "+" + errors + " errors, ";
		}
		return
				transmitted + " packets transmitted, " 
				+ received + " received, "
				+ err
				+ loss + "% packet loss, "
				+ "time " + time + "ms";
	}
}
