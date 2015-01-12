package xingu.validator.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexValidatorSupport
    extends ValidatorSupport
{
	@Override
	public boolean apply(Object bean, String value)
    {
        Pattern pattern;
        String expression = getExpression();
        if (isCaseSensitive()) 
        {
            pattern = Pattern.compile(expression);
        } 
        else 
        {
            pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(value);
        return matcher.matches(); 
    }

	protected boolean isCaseSensitive()
    {
	    return false;
    }

	protected abstract String getExpression();
}
