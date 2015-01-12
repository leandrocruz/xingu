package xingu.utils.br;

public class Renavam
{
	public static final String EXAMPLE = "13.791647-7";
	
	public static String calcDigVerif(String num)
	{
		int primDig = 0;

        int soma = 0;
        int mul = 2;
        
		for (int j = num.length() - 1; j > 0; j--)
		{
			soma += mul++ * Integer.parseInt(num.substring(j - 1, j));
		}
            
        primDig = ((soma % 10) % 11);            

		return Integer.toString(primDig);
	}
	
	public static String geraRenavam()
	{
		return geraRenavam(null);
	}
	
	public static String geraRenavam(String start)
	{
		if(start == null)
		{
			start = "";
			Integer numero;
			for (int i = 0; i < 8; i++)
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
