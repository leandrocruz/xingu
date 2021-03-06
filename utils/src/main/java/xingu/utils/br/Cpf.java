package xingu.utils.br;

public class Cpf
{
	public static final String EXAMPLE = "123.123.123-87";
	
	public static boolean isValid(String num)
	{
		num = clearMask(num);
		int len = num.length();
		if(len != 11)
		{
			return false;
		}
		
		String prefix   = num.substring(0, 9);
		String dig      = num.substring(9);
		String expected = calcDigVerif(prefix);
		return expected.equals(dig);
	}
	
	public static String calcDigVerif(String num)
	{
		Integer primDig, segDig;
		
		int soma = 0, peso = 10;
		for (int i = 0; i < num.length(); i++)
		{
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}
		if (soma % 11 == 0 | soma % 11 == 1)
		{
			primDig = new Integer(0);
		}
		else
		{
			primDig = new Integer(11 - (soma % 11));
		}

		soma = 0; peso = 11;
		for (int i = 0; i < num.length(); i++)
		{
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		soma += primDig.intValue() * 2;
		if (soma % 11 == 0 | soma % 11 == 1)
		{
			segDig = new Integer(0);
		}
		else
		{
			segDig = new Integer(11 - (soma % 11));
		}

		return Integer.toString(primDig) + Integer.toString(segDig);
	}
	
	public static String geraCPF()
	{
		return geraCPF(null);
	}
	
	public static String geraCPF(String start)
	{
		if(start == null)
		{
			start = "";
			Integer numero;
			for (int i = 0; i < 9; i++)
			{
				numero = new Integer((int) (Math.random() * 10));
				start += numero.toString();
			}
		}
		else
		{
			start = clearMask(start);
		}
		return start + calcDigVerif(start);
	}

	public static String clearMask(String s)
	{
		return s.replaceAll("[^0-9]*", "");
	}

	public static String addMask(String s)
	{
		s = clearMask(s);
		StringBuffer sb = new StringBuffer();
		char[] array = s.toCharArray();
		for (int i = 0; i < 11; i++)
		{
			char c = array[i];
			sb.append(c);
			if(i == 2 || i == 5)
			{
				sb.append(".");
			}
			else if (i == 8)
			{
				sb.append("-");
			}
		}
		return sb.toString();
	}
}
