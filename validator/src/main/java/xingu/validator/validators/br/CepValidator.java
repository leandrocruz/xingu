package xingu.validator.validators.br;

import xingu.validator.ann.br.ValidateCep;
import xingu.validator.validators.RegexValidatorSupport;

public class CepValidator
	extends RegexValidatorSupport
{
    public static final String pattern = "\\d{5}\\-\\d{3}";

	public CepValidator(ValidateCep ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
	}

	@Override
    protected String getExpression()
    {
	    return pattern;
    }
}
