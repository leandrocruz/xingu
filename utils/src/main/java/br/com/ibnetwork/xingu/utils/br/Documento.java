package br.com.ibnetwork.xingu.utils.br;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class Documento
{
	private String	value;

	private int		len;

	public static enum Type { UNKNOWN, CPF, CNPJ } ;
	
	public Documento(String value)
	{
		if(StringUtils.isEmpty(value))
		{
			this.value = null;
		}
		else
		{
			this.value = clearMask(value);
			this.len   = this.value.length();
		}
	}

	public boolean isValid()
	{
		if(isCpf())
		{
			return Cpf.isValid(getValue());
		}
		throw new NotImplementedYet();
	}
	
	public boolean isCnpj()
	{
		return len == 14;
	}

	public boolean isCpf()
	{
		return len == 11;
	}

	public static String clearMask(String s)
	{
		return s.replaceAll("[^0-9]*", "");
	}

	public Type getType()
	{
		if(isCpf())
		{
			return Type.CPF;
		}
		else if(isCnpj())
		{
			return Type.CNPJ;
		}
		else
		{
			return Type.UNKNOWN;
		}
	}
	
	public String getValue()
	{
		return value;
	}

	public String getValueFormatted()
	{
		if(isCpf())
		{
			return Cpf.addMask(getValue());
		}
		throw new NotImplementedYet();
	}

	@Override
	public String toString()
	{
		return this.value;
	}
}