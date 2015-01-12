package xingu.utils.clone;

public class WithInterface
{
	private IFace iFace;
	
	public WithInterface()
	{}
	
	public WithInterface(IFace iFace)
	{
		this.iFace = iFace;
	}

	public IFace iFace()
	{
		return iFace;
	}
}