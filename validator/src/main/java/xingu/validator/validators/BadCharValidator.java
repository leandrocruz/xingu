package xingu.validator.validators;

import xingu.validator.ann.ValidateBadChar;

public class BadCharValidator
    extends ValidatorSupport
{
	private ValidateBadChar _ann;
	
	public BadCharValidator(ValidateBadChar ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
		_ann = ann;
	}

	@Override
    public boolean apply(Object bean, String value)
    {
		return value.indexOf(_ann.badChar()) < 0;
    }
}
