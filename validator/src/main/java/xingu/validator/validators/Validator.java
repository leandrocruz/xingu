package xingu.validator.validators;

public interface Validator
{
	boolean apply(Object bean, Object value);
	
	boolean apply(Object bean, String value);

	boolean trimIfString();
	
	boolean acceptNullValue();
	
	String getMessageId();
	
	String getMessage();
}
