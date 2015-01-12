package xingu.validator.validators;

import org.apache.commons.beanutils.ConvertUtils;

import xingu.validator.ann.ValidateMinValue;


public class MinValueValidator
    extends ValidatorSupport
{
	ValidateMinValue _ann;
	
	public MinValueValidator(ValidateMinValue ann)
	{
		_ann = ann;
		_message = ann.message();
		_messageId = ann.messageId();
	}

	@SuppressWarnings("unchecked")
    @Override
    public boolean apply(Object bean, Object value)
    {
		String s = String.valueOf(_ann.minValue());
		Class<? extends Object> clazz = value.getClass();
		Comparable min = (Comparable) ConvertUtils.convert(s,clazz);
		return min.compareTo(value) < 0;
    }
}
