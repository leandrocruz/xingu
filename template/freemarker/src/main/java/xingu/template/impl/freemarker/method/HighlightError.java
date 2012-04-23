package xingu.template.impl.freemarker.method;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import xingu.template.TemplateEngineException;


import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class HighlightError
    implements TemplateMethodModelEx
{

    public Object exec(List list) 
        throws TemplateModelException
    {
        String text = ((SimpleScalar) list.get(0)).getAsString();
        int contextLines = ((SimpleNumber) list.get(2)).getAsNumber().intValue();
        
        Object obj = list.get(1);
        BeanModel model = (BeanModel) obj;
        TemplateEngineException tee = (TemplateEngineException) model.getWrappedObject(); 

        int lineNumber = tee.getLineNumber();
        String expression = tee.getExpression();
        
        int lineCount = 1;
        
        String[] lines = text.split("\n");
    	StringBuffer result = new StringBuffer();
    	int fromLine = 1;
    	int toLine = lines.length+1;
    	if(contextLines > 0)
    	{
    		fromLine = lineNumber - contextLines;
    		toLine = lineNumber + contextLines;
    	}
    	//System.out.println("from: "+fromLine+" to: "+toLine);
        for (String line : lines)
        {
        	
        	//System.out.println("Line("+lineCount+") -> "+line);
    		//skip this line?
        	if(lineCount < fromLine || lineCount > toLine)
        	{

        		if(lineCount == fromLine -1 || lineCount == toLine + 1)
        		{
        			result.append(lineCount)
        			.append(": ...\n");
        		}
        		lineCount++;
        		continue;
        	}
        		
        	line = StringEscapeUtils.escapeHtml(line);
    		if(lineCount == lineNumber)
    		{
    			line = line.replace("${"+expression+"}", "<b>${"+expression+"}</b>");
    			result.append("<span class=\"error\">")
    				.append(lineCount)
    				.append(": ")
    				.append(line)
    				.append("</span>");
    		}
    		else
    		{
    			result.append(lineCount)
    			.append(": ")
    			.append(line);
    		}
            lineCount++;
            result.append("\n");
        }
        return result.toString();
    }

}
