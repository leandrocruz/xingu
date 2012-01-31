package br.com.ibnetwork.xingu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.test.SampleBean;
import br.com.ibnetwork.xingu.validator.BeanValidator;
import br.com.ibnetwork.xingu.validator.ValidatorContext;
import br.com.ibnetwork.xingu.validator.ValidatorResult;

public class ValidatorTest
    extends XinguTestCase
{
	@Inject
	private BeanValidator validator;

	@Test
	public void testValidateNullBean()
		throws Exception
	{
		//run validator
		ValidatorContext ctx = new ValidatorContext();
		boolean isValid = validator.validate(SampleBean.class, ctx);
		assertFalse(isValid);

		ValidatorResult result = null;

		result = ctx.get("name"); 
		assertEquals(false, result.isValid());

		result = ctx.get("lastName"); 
		assertEquals(false, result.isValid());

		result = ctx.get("email"); 
		assertEquals(true, result.isValid());

		result = ctx.get("username"); 
		assertEquals(true, result.isValid());

		result = ctx.get("cep"); 
		assertEquals(true, result.isValid());

	}
	
	@Test
	public void testValidateRequired()
		throws Exception
	{
		//create bean
		SampleBean bean = new SampleBean();
		bean.setName("myName");
		
		//run validator
		ValidatorContext ctx = new ValidatorContext();
		boolean isValid = validator.validate(bean, ctx);
		
		//asserts
		assertEquals(false, isValid);
		assertEquals(17,ctx.getFields().length);
		assertEquals(2,ctx.getErrorCount());
		
		ValidatorResult result = null;
		
		result = ctx.get("name"); 
		assertEquals(true, result.isValid());
		result = ctx.get("email"); 
		assertEquals(true, result.isValid());
		result = ctx.get("lastName"); 
		assertEquals(false, result.isValid());
		assertEquals("Field required", result.getValidator().getMessage());
		
		//test empty string
		bean.setName("");
		ctx = new ValidatorContext();
		validator.validate(bean, ctx);
		result = ctx.get("name"); 
		assertEquals(false, result.isValid());

		bean.setName("  ");
		ctx = new ValidatorContext();
		validator.validate(bean, ctx);
		result = ctx.get("name"); 
		assertEquals(false, result.isValid());

	}
	
	@Test
	public void testValidateEmail()
		throws Exception
	{
		//no email
		SampleBean bean = new SampleBean();
		validate("email",bean,true);
		
		//bad email
		bean.setEmail("email");
		ValidatorResult result = validate("email",bean,false);
		assertEquals("Bad email syntax", result.getValidator().getMessage());

		//empty email
		bean.setEmail(" ");
		validate("email",bean,true);

		//good email
		bean.setEmail("email@e.co");
		validate("email",bean,true);
	}
	
	@Test
	public void testValidateJava()
		throws Exception
	{
		SampleBean bean = new SampleBean();
		bean.setUsername("not ok");
		validate("username",bean,false);
		
		bean.setUsername("ok");
		validate("username",bean,true);
	}

	@Test
	public void testValidateCep()
		throws Exception
	{
		SampleBean bean = new SampleBean();
		validate("cep", bean, true);

		bean.setCep("");
		validate("cep", bean, true);

		bean.setCep(" ");
		validate("cep", bean, true);

		bean.setCep("12345-123");
		validate("cep", bean, true);

		bean.setCep("xyz");
		validate("cep", bean, false);
	}

	@Test
	public void testValidateMinValue()
		throws Exception
	{
		SampleBean bean = new SampleBean();
		validate("minValue",bean,false);
		
		bean.setMinValue(10);
		validate("minValue",bean,false);

		bean.setMinValue(11);
		validate("minValue",bean,true);
	}

	@Test
	public void testValidateCpf()
		throws Exception
	{
		SampleBean bean = new SampleBean();
		bean.setCpf("123.123.123-00");
		validate("cpf",bean,false);

		bean.setCpf("0");
		validate("cpf",bean,false);

		bean.setCpf("511.481.301-36");
		validate("cpf",bean,true);

		bean.setCpf("51148130136");
		validate("cpf",bean,true);

		bean.setCpf("511.491.301-36");
		validate("cpf",bean,false);

		bean.setCpf("511.481.301-37");
		validate("cpf",bean,false);
	}
    
	@Test
    public void testValidateRg()
        throws Exception
    {
        SampleBean bean = new SampleBean();
        bean.setRg("1.123.123-0");
        validate("rg",bean,false);
    
        bean.setRg("0");
        validate("rg",bean,false);
    
        bean.setRg("30.204.535-1");
        validate("rg",bean,true);
        
        bean.setRg("4.415.257-6");
        validate("rg",bean,true);
        
        bean.setRg("5.846.162-0");
        validate("rg",bean,true);
        
        //bean.setRg("7.622.149-9");
        //validate("rg",bean,true);
    
        bean.setRg("302045351");
        validate("rg",bean,true);
    
        bean.setRg("30.204.545-1");
        validate("rg",bean,false);
    
        bean.setRg("30.204.535-2");
        validate("rg",bean,false);
    }
    
	@Test
    public void testValidatePlaca()
        throws Exception
    {
        SampleBean bean = new SampleBean();
        validate("placa", bean, true);
    
        bean.setPlaca("");
        validate("placa", bean, true);
    
        bean.setPlaca(" ");
        validate("placa", bean, true);
    
        bean.setPlaca("BER 2233");
        validate("placa", bean, true);
    
        bean.setPlaca("BER-2233");
        validate("placa", bean, true);        
        
        bean.setPlaca("xyz");
        validate("placa", bean, false);
    }
    
	@Test
    public void testValidateRenavam()
        throws Exception
    {
        SampleBean bean = new SampleBean();
        validate("renavam", bean, true);
    
        bean.setRenavam("");
        validate("renavam", bean, true);
    
        bean.setRenavam(" ");
        validate("renavam", bean, true);
        
        
        bean.setRenavam("42.296461-1");
        validate("renavam", bean, true);
        
        bean.setRenavam("13.791647-7");
        validate("renavam", bean, true);
    
        bean.setRenavam("xyz");
        validate("renavam", bean, false);
    }
	
	@Test
    public void testValidateBadChar()
    	throws Exception
    {
        SampleBean bean = new SampleBean();
        validate("badChar", bean, true);
        
        bean.setBadChar("aaa");
        validate("badChar", bean, true);
        
        bean.setBadChar("aaabc");
        validate("badChar", bean, false);
    }

	@Test
    public void testValidatePastBefore()
		throws Exception
	{
    	SampleBean bean = new SampleBean();
    	validate("pastBefore", bean, true);
    	
    	Date today = new Date();
    	DateTime date = new DateTime(today);
    	
    	bean.setPastBefore(today);
    	validate("pastBefore", bean, false);
    	
    	bean.setPastBefore(date.minus(Period.days(300)).toDate());
    	validate("pastBefore", bean, false);

    	bean.setPastBefore(date.minus(Period.days(400)).toDate());
    	validate("pastBefore", bean, true);
	}

	@Test
    public void testValidatePastAfter()
		throws Exception
	{
    	SampleBean bean = new SampleBean();
    	Date today = new Date();
    	DateTime date = new DateTime(today);
	
    	bean.setPastAfter(today);
    	validate("pastAfter", bean, true);
	
    	bean.setPastAfter(date.minus(Period.days(300)).toDate());
    	validate("pastAfter", bean, true);

    	bean.setPastAfter(date.minus(Period.days(400)).toDate());
    	validate("pastAfter", bean, false);
	}

	@Test
    public void testValidateFutureAfter()
		throws Exception
	{
    	SampleBean bean = new SampleBean();
    	Date today = new Date();
    	DateTime date = new DateTime(today);

    	bean.setFutureAfter(today);
    	validate("futureAfter", bean, false);

    	bean.setFutureAfter(date.plus(Period.days(300)).toDate());
    	validate("futureAfter", bean, false);

    	bean.setFutureAfter(date.plus(Period.days(400)).toDate());
    	validate("futureAfter", bean, true);
	}

	@Test
    public void testValidateFutureBefore()
		throws Exception
	{
    	SampleBean bean = new SampleBean();
    	Date today = new Date();
    	DateTime date = new DateTime(today);

    	bean.setFutureBefore(today);
    	validate("futureBefore", bean, true);

    	bean.setFutureBefore(date.plus(Period.days(300)).toDate());
    	validate("futureBefore", bean, true);

    	bean.setFutureBefore(date.plus(Period.days(400)).toDate());
    	validate("futureBefore", bean, false);
	}

	@Test
    public void testValidatePastBeforeOnNow()
		throws Exception
	{
    	SampleBean bean = new SampleBean();
    	Date today = new Date();
    	DateTime date = new DateTime(today);

        bean.setNow(date.minus(Period.seconds(1)).toDate()); //XXX: try to cope with timing problems
    	validate("now", bean, true);

    	bean.setNow(date.plus(Period.days(1)).toDate());
    	validate("now", bean, false);

    	bean.setNow(date.minus(Period.days(1)).toDate());
    	validate("now", bean, true);
	}

	private ValidatorResult validate(String fieldName, Object bean, boolean expected)
    	throws Exception
	{
		ValidatorContext ctx = new ValidatorContext();
		validator.validate(bean, ctx);
		ValidatorResult result = ctx.get(fieldName);
		assertEquals(expected, result.isValid());
		return result;
    }
}