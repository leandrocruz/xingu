package br.com.ibnetwork.xingu.validator.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateBadChar
{
	String message() default "";
	String messageId() default "badChar";
	String badChar();
}
