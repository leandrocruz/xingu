package br.com.ibnetwork.xingu.utils.backtrack;

public class Point
{
	int status;
	
	int value;

	public Point(int status, int value)
    {
		this.status = status;
		this.value = value;
    }

	@Override
    public String toString()
    {
	    return String.valueOf(status);
    }
}
