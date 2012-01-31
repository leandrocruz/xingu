package br.com.ibnetwork.xingu.validator.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.validator.BeanValidator;
import br.com.ibnetwork.xingu.validator.ValidatorContext;
import br.com.ibnetwork.xingu.validator.ValidatorFactory;
import br.com.ibnetwork.xingu.validator.ValidatorResult;
import br.com.ibnetwork.xingu.validator.ann.ValidateRequired;
import br.com.ibnetwork.xingu.validator.validators.Validator;


public class BeanValidatorImpl
	implements BeanValidator
{
	private Logger log = LoggerFactory.getLogger(BeanValidatorImpl.class);
	
	@Inject
	private Factory factory;

	public boolean validate(Class beanClass, ValidatorContext ctx) 
		throws Exception
	{
		return runValidator(beanClass, null, ctx);
	}
	
	public boolean validate(Object bean, ValidatorContext ctx) 
		throws Exception
	{
		return runValidator(bean.getClass(), bean, ctx);
	}
	
	private boolean runValidator(Class beanClass, Object bean, ValidatorContext ctx) 
		throws Exception
    {
		boolean isValid = true;
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(beanClass);
		for (PropertyDescriptor descriptor : properties)
        {
			String name = descriptor.getName();
			Object value = getFieldValue(bean, descriptor);
			Field field = FieldUtils.getField(beanClass, name);
			if(field != null)
			{
				ValidatorResult result = validateField(bean, field, value);
				ctx.put(name, result);
		        isValid =  result.isValid() && isValid;
			}
        }
		return isValid;
    }

	private ValidatorResult validateField(Object bean, Field field, Object value)
    {
		String fieldName = field.getName();
		ValidateRequired required = field.getAnnotation(ValidateRequired.class);
		log.debug("Field: "+fieldName+", value: "+value+" is required ? "+(required != null));
        ValidatorResult result = new ValidatorResult(fieldName, value);
		if(value != null)
		{
			List<Validator> validators = parseConstraints(field);
			for (Validator validator : validators)
            {
				boolean isValid;
				if(value instanceof String)
				{
					String s = (String) value;
					if(validator.trimIfString())
					{
						s = StringUtils.trimToNull(s);
					}
					if(s == null && validator.acceptNullValue())
					{
						isValid = true;
					}
					else
					{
						isValid = validator.apply(bean, s);	
					}
				}
				else
				{
					isValid = validator.apply(bean, value);
				}
				if(log.isDebugEnabled())
	            {
	            	log.debug("Validator applied: "+validator.getClass().getName()+" to: "+value+" result: "+isValid);
	            }
				if(!isValid)
	            {
	    			result.setValidator(validator);
	    			result.setValid(false);
	            }
            }
		}
		else if(required != null && required.required())
		{
			result.setValidator(ValidatorFactory.createFromAnnotation(required,factory));
			result.setValid(false);
		}
		return result;
    }

	private List<Validator> parseConstraints(Field field)
    {
		Annotation[] annotations = field.getAnnotations();
		List<Validator> result = new ArrayList<Validator>();
		for (int i = 0; i < annotations.length; i++)
        {
            Annotation ann = annotations[i];
            Validator constraint = ValidatorFactory.createFromAnnotation(ann, factory);
            if(constraint != null)
            {
            	result.add(constraint);
            }
        }
		return result;
    }


	private Object getFieldValue(Object bean, PropertyDescriptor descriptor) 
		throws Exception
	{
		if(bean == null)
		{
			return null;
		}
		
		Method readMethod = descriptor.getReadMethod();
		if(readMethod == null)
		{
			return null;
		}
		Object result = readMethod.invoke(bean,(Object[])null);
		return result;
	}
}