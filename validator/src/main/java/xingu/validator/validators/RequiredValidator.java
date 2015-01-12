package xingu.validator.validators;

import xingu.validator.ann.ValidateRequired;


public class RequiredValidator
    extends ValidatorSupport
{
	public RequiredValidator(ValidateRequired ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
	}
	
	@Override
	public boolean apply(Object bean, String value)
	{
		return value != null;
	}

	@Override
    public boolean apply(Object bean, Object value)
    {
		return value != null;
    }
	
	public boolean acceptNullValue()
    {
	    return false;
    }
}
