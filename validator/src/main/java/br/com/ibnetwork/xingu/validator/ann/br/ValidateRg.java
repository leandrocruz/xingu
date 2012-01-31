package br.com.ibnetwork.xingu.validator.ann.br;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateRg
{
	String message() default "";
	String messageId() default "badRg";
}
