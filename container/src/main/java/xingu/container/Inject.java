package xingu.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Pulga uses this annotation to inject dependencies into it's components
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject 
{
    String value() default ""; //used to determine the component key 
    boolean lazy() default false;
}
