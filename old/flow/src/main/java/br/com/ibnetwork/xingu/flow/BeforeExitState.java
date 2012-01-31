package br.com.ibnetwork.xingu.flow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeExitState
{
	String state ();
}