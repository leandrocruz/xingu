package br.com.ibnetwork.xingu.validator.validators;


public abstract class ValidatorSupport
    implements Validator
{
	protected String _message;
	
	protected String _messageId;

	public String getMessage()
	{
	    return _message;
	}
	
	public String getMessageId()
    {
    	return _messageId;
    }

	public boolean apply(Object bean, String value)
    {
	    return false;
    }
	
	public boolean apply(Object bean, Object value)
    {
	    return false;
    }

	public boolean trimIfString()
    {
	    return true;
    }

	public boolean acceptNullValue()
    {
	    return true;
    }
}