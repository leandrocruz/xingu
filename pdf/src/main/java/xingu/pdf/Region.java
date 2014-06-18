package xingu.pdf;

public class Region
{
	double center;
	
	double radius;
	
	public Region(double center, double radius)
	{
		this.center = center;
		this.radius = radius;
	}

	public boolean inRadius(double point)
	{
		return center - radius < point && center + radius > point;
	}
	
	@Override
	public String toString()
	{
		return "(" + center + " ±" + radius + ")";
	}
}
