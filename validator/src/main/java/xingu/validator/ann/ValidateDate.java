package xingu.validator.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateDate
{
	public enum Comparator {BEFORE, BEFORE_OR_EQUAL, AFTER, AFTER_OR_EQUAL};
	public enum Direction {PAST, FUTURE};
	String message() default "";
	String messageId() default "badDate";
	Comparator comparator() default Comparator.BEFORE;
	Direction direction() default Direction.PAST;
	String date() default "0d"; //10y (10 years), 2m (2 months), 3d (3 days)
}
