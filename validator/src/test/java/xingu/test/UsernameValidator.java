package xingu.test;

import xingu.validator.ann.ValidateJava;
import xingu.validator.validators.ValidatorSupport;

public class UsernameValidator
    extends ValidatorSupport
{
	public UsernameValidator(ValidateJava ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
	}

	@Override
	public boolean apply(Object bean, String value)
	{
		String username = (String) value;
		return "ok".equals(username);
	}

}
