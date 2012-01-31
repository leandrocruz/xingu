package br.com.ibnetwork.xingu.validator.validators;

import br.com.ibnetwork.xingu.validator.ann.ValidateEmail;

public class EmailValidator
	extends RegexValidatorSupport
{
    public static final String emailAddressPattern =
    	"\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";

	public EmailValidator (ValidateEmail ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
	}

	@Override
    protected String getExpression()
    {
	    return emailAddressPattern;
    }
}
