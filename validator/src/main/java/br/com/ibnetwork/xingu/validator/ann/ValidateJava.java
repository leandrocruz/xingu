package br.com.ibnetwork.xingu.validator.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.ibnetwork.xingu.validator.validators.Validator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateJava
{
	String message() default "";
	String messageId() default "badValue";
	Class<? extends Validator> validatorClass();
}
