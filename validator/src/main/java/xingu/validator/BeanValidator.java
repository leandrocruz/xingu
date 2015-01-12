package xingu.validator;

public interface BeanValidator
{
	boolean validate(Object bean, ValidatorContext ctx) 
		throws Exception;
	
	boolean validate(Class beanClass, ValidatorContext ctx) 
		throws Exception;
}
