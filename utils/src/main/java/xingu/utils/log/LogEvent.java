package xingu.utils.log;

import java.util.Date;
import java.util.List;

public interface LogEvent
{
	/*
	 * I know, Type should be an enum, but Xoia can't decode it, yet.
	 */
	class Type {
		
		public static Type TRACE = new Type("TRACE");
		public static Type DEBUG = new Type("DEBUG");
		public static Type INFO  = new Type("INFO");
		public static Type WARN  = new Type("WARN");
		public static Type ERROR = new Type("ERROR");

		private String name;
		
		private Type(String name)
		{
			this.name = name;
		}

		/* Required by Xoia */
		public Type(){}
		public String getName(){return name;}
		public void setName(String name){this.name = name;}
	};

	Date getDate();

	Type getType();

	String getMessage();

	String getFormat();

	Throwable getError();

	List<Object> getArgs();
}