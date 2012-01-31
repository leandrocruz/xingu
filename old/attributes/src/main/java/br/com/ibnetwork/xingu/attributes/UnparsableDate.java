package br.com.ibnetwork.xingu.attributes;

import java.util.Date;

public class UnparsableDate
	extends Date
{
	public static final UnparsableDate INSTANCE = new UnparsableDate();
	private String format;

    private String unparsable;

    public UnparsableDate(String value, String format)
	{
		super();
		this.unparsable = value;
		this.format = format;
	}
	
	public UnparsableDate()
    {
    	super(0);
    }

    public String getUnparsable()
	{
		return unparsable;
	}
	
	public String getFormat()
	{
		return format;
	}
}
