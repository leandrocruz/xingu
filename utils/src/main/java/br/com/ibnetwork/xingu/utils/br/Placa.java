package br.com.ibnetwork.xingu.utils.br;

public class Placa
{
	public static final String EXAMPLE = "AAA-3434";
	
	public static boolean validaPlaca(String num)
	{
        boolean result = true;
        
        if (num.length() >= 3)
        {
    		for (int i = 0; i < 3; i++)
    		{
    			result &= Character.isLetter(num.charAt(i));
    		}
            
            for (int i = 3; i < num.length(); i++)
            {
                result &= Character.isDigit(num.charAt(i));
            }
        }
        else
        {
            result = false;
        }
        
		return result;
	}

	public static String clearMask(String s)
	{
		return s.replaceAll("[^a-zA-Z0-9]*", "");
	}

	public static String addMask(String s)
	{
		return null;
	}
}
