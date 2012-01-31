package br.com.ibnetwork.xingu.validator;

import br.com.ibnetwork.xingu.validator.validators.Validator;

public class ValidatorResult
{
	private String fieldName;
	
	private Object fieldValue;

	private boolean valid = true;
	
	private Validator validator;
	
	public ValidatorResult(String fieldName, Object fieldValue)
    {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
    }

	public boolean isValid(){return valid;}
	public void setValid(boolean isValid){this.valid = isValid;}

	public Validator getValidator(){return validator;}
	public void setValidator(Validator constraint){this.validator = constraint;}
}
