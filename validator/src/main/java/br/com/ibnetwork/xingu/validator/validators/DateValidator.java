package br.com.ibnetwork.xingu.validator.validators;

import org.joda.time.DateTime;
import org.joda.time.Period;

import br.com.ibnetwork.xingu.validator.ann.ValidateDate;
import br.com.ibnetwork.xingu.validator.ann.ValidateDate.Comparator;
import br.com.ibnetwork.xingu.validator.ann.ValidateDate.Direction;



public class DateValidator
    extends ValidatorSupport
{
	ValidateDate _ann;
	
	public DateValidator(ValidateDate ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
		_ann = ann;
	}
	
    @Override
    public boolean apply(Object bean, Object value)
    {
		Period period = getPeriod(_ann.date());
		Direction direction = _ann.direction();
		DateTime dateTime = new DateTime();
		DateTime point;
		if(direction == Direction.PAST)
		{
			point = dateTime.minus(period);
		}
		else
		{
			point = dateTime.plus(period);
		}
		Comparator comparator = _ann.comparator();
		
		DateTime date = new DateTime(value);
		//System.out.println("Date: "+date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/"+date.getYear());
		boolean result;
		if(comparator == Comparator.BEFORE)
		{
			//System.out.println("Point(BEFORE): "+point.getDayOfMonth() + "/" + point.getMonthOfYear() + "/"+point.getYear());
			result = date.isBefore(point); 
		}
		else
		{
			//System.out.println("Point(AFTER): "+point.getDayOfMonth() + "/" + point.getMonthOfYear() + "/"+point.getYear());
			result = date.isAfter(point);
		}
		return result;
    }

	private Period getPeriod(String date)
    {
		String s = date.substring(0, date.length() - 1);
		int num = Integer.parseInt(s);
		if(date.endsWith("y"))
		{
			return Period.years(num);
		}
		else if(date.endsWith("m"))
		{
			return Period.months(num);
		}
		else if(date.endsWith("d"))
		{
			return Period.days(num);
		}
		else
		{
			return Period.ZERO;
		}
    }
}
