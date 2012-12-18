package xingu.template;

public class TemplateEngineException
        extends RuntimeException
{
	private String templateName;
	
	private String fileName;
	
	private int lineNumber;
	
	private int columnNumber;
	
	private String expression;
	
    public TemplateEngineException(String message, Throwable t)
    {
        super(message,t);
    }

    public TemplateEngineException(String message)
    {
        super(message);
    }

	public int getColumnNumber()
    {
    	return columnNumber;
    }

	public void setColumnNumber(int column)
    {
    	this.columnNumber = column;
    }

	public int getLineNumber()
    {
    	return lineNumber;
    }

	public void setLineNumber(int line)
    {
    	this.lineNumber = line;
    }

	public String getTemplateName()
    {
    	return templateName;
    }

	public void setTemplateName(String templateName)
    {
    	this.templateName = templateName;
    }

	public String getFileName()
    {
    	return fileName;
    }

	public void setFileName(String fileName)
    {
    	this.fileName = fileName;
    }

	public String getExpression()
    {
    	return expression;
    }

	public void setExpression(String expression)
    {
    	this.expression = expression;
    }
}
