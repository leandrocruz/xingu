package xingu.utils.printer;

import org.apache.commons.lang3.StringUtils;

public abstract class PrinterSupport
	implements Printer
{
	protected int			depth	= 0;

	protected StringBuffer	sb		= new StringBuffer();

	protected String		ident	= "\t";

	public PrinterSupport(String ident)
	{
		this.ident = ident;
	}

	@Override
	public Printer increment()
	{
		depth++;
		return this;
	}

	@Override
	public Printer decrement()
	{
		depth--;
		return this;
	}
	
	@Override
	public Printer br()
	{
		sb.append("\n");
		return this;
	}

	@Override
	public Printer value(Object value)
	{
		sb.append(value);
		return this;
	}

	@Override
	public Printer ident()
	{
		sb.append(StringUtils.repeat(ident, depth));
		return this;
	}

	@Override
	public Printer attr(String name, int value)
	{
		attr(name, String.valueOf(value));
		return this;
	}

	@Override
	public Printer attrIf(String name, String value)
	{
		if(value != null)
		{
			attr(name, value);
		}
		return this;
	}

	@Override
	public String toString()
	{
		return sb.length() == 0 ? null : sb.toString();
	}
}
