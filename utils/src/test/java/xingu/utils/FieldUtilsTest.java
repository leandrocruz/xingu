package xingu.utils;

import java.lang.reflect.Field;
import java.util.List;

import xingu.utils.FieldUtils;
import xingu.utils.test.ObjectImpl;
import junit.framework.TestCase;

public class FieldUtilsTest
    extends TestCase
{
	public void testGetAllFields()
		throws Exception
	{
		List<Field> fields = FieldUtils.getAllFields(ObjectImpl.class);
		assertEquals(3,fields.size());
	}
	
	public void testGetFieldByName()
		throws Exception
	{
		Field id = FieldUtils.getField(ObjectImpl.class,"id");
		assertEquals("id",id.getName());
	}
}
