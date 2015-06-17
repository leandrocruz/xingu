package xingu.type.transformer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MoneyTransformer
	implements Transformer
{
	private Locale from = Locale.US;

	private Locale to = new Locale("pt", "BR");

	@Override
	public String transform(String value)
	{
		if(value == null)
		{
			return null;
		}
		try
		{
			Number number = NumberFormat.getInstance(from).parse(value);
			NumberFormat format = NumberFormat.getNumberInstance(to);
			DecimalFormat df = DecimalFormat.class.cast(format);
			df.applyPattern("###,###.00");
			String result = df.format(number);
			return result;
		}
		catch(ParseException e)
		{
			return null;
		}
	}
}
