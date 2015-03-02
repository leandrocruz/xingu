package xingu.type.impl;

import org.apache.commons.lang3.StringUtils;

import xingu.type.Transformer;

public class PadTransformer
	implements Transformer
{
	private String direction;
	
	private int length;
	
	private char character = '\0';
	
	@Override
	public String transform(String value)
	{
		switch(direction)
		{
			case "left":
				return StringUtils.leftPad(value, length, character);
			
			default:
				return StringUtils.rightPad(value, length, character);
		}
	}

	public void setDirection(String direction){this.direction = direction;}
	public void setLength(int length){this.length = length;}
	public void setCharacter(char character){this.character = character;}
}
