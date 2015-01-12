package xingu.utils;

import java.util.HashMap;
import java.util.Map;

public class JdbcUtils
{
	public static Map<String, String> samples = new HashMap<String, String>();
	
	public static Map<String, String> conversions = new HashMap<String, String>();
	
	public static Map<String, String> nullValue = new HashMap<String, String>();
	
	static
	{
		samples.put("BIT", 			"0");
		samples.put("TINYINT", 		"1");
		samples.put("SMALLINT", 	"1");
		samples.put("INT", 			"1");
		samples.put("BIGINT", 		"1");
		samples.put("FLOAT", 		"1");
		samples.put("DOUBLE", 		"1");
		samples.put("DECIMAL", 		"1");
		samples.put("DATE", 		"'2010-02-01'");
		samples.put("TIME", 		"'00:00:00'");
		samples.put("DATETIME", 	"'2010-02-01 00:00:00.00'");
		samples.put("TIMESTAMP", 	"'2010-02-01 00:00:00.00'");
		samples.put("VARCHAR", 		"'sample'");
		
		conversions.put("boolean", 				"BIT");
		conversions.put("java.lang.Boolean", 	"BIT");
		conversions.put("byte", 				"TINYINT");
		conversions.put("java.lang.Byte", 		"TINYINT");
		conversions.put("short", 				"SMALLINT");
		conversions.put("java.lang.Short", 		"SMALLINT");
		conversions.put("int", 					"INT");
		conversions.put("java.lang.Integer", 	"INT");
		conversions.put("long", 				"BIGINT");
		conversions.put("java.lang.Long", 		"BIGINT");
		conversions.put("float", 				"FLOAT");
		conversions.put("java.lang.Float", 		"FLOAT");
		conversions.put("double", 				"DOUBLE");
		conversions.put("java.lang.Double", 	"DOUBLE");
		conversions.put("java.math.BigDecimal", "DECIMAL");
		conversions.put("java.util.Date", 		"TIMESTAMP");
		conversions.put("java.lang.String",		"VARCHAR");
	
		nullValue.put("BIT", 		"false");
		nullValue.put("TINYINT", 	"0");
		nullValue.put("SMALLINT", 	"0");
		nullValue.put("INT", 		"0");
		nullValue.put("BIGINT", 	"0");
		nullValue.put("FLOAT", 		"0");
		nullValue.put("DOUBLE", 	"0");
		nullValue.put("DECIMAL", 	"0");
		nullValue.put("DATE", 		"NULL");
		nullValue.put("TIME", 		"NULL");
		nullValue.put("DATETIME", 	"NULL");
		nullValue.put("TIMESTAMP", 	"NULL");
		nullValue.put("VARCHAR", 	"NULL");
	}

    public static String toColumnName(String name) 
    {
    	char[] array = name.toCharArray();
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < array.length; i++)
        {
	        char c = array[i];
	        if(Character.isUpperCase(c))
	        {
	        	sb.append("_");
	        }
	        c = Character.toUpperCase(c);
	        sb.append(c);	
        }
    	return sb.toString();
    }
}
