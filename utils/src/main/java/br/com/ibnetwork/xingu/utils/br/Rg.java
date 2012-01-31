package br.com.ibnetwork.xingu.utils.br;


public class Rg
{
	public static final String EXAMPLE = "123.123.123-87";
	
	public static boolean calcDigVerif(String rg)
	{
        int a = rg.charAt(0) - '0';
        int b = rg.charAt(1) - '0';
        int c = rg.charAt(2) - '0';
        int d = rg.charAt(3) - '0';
        int e = rg.charAt(4) - '0';
        int f = rg.charAt(5) - '0';
        int g = rg.charAt(6) - '0';
        int h = rg.charAt(7) - '0';
        int i = rg.charAt(8) - '0';

        int result = 2 * a + 3 * b + 4 * c + 5 * d + 6 * e + 7 * f + 8 * g + 9 * h + 100 * i;
        return (result % 11) % 10 == 0;
	}
	
	public static String geraRG()
	{
		return geraRG(null);
	}
	
	public static String geraRG(String start)
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
		return null;
	}
}
