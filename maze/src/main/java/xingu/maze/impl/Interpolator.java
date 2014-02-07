package xingu.maze.impl;

import java.util.Map;
import java.util.Stack;

public class Interpolator
{
    public static String interpolate(String resource)
    {
        return interpolate(resource, null);
    }
    
    public static String interpolate(String resource, Map<String, String> map)
    {
        if(resource.indexOf("$") < 0)
        {
            int index;
            String var = resource;
            String defaultValue = null;
            if((index = resource.indexOf("?")) > 0)
            {
                 var = resource.substring(0, index);
                 defaultValue = resource.substring(index + 1);
            }
            String value = System.getProperty(var);
            if(value == null && map != null)
            {
                value = map.get(var);
            }
            if(value == null && defaultValue != null)
            {
                value = defaultValue;
            }
            return value != null ? value : resource;
        }
        char[] array = resource.toCharArray();
        Stack<Block> stack = new Stack<Block>();
        stack.add(new Block(0));
        for(int i=0 ; i<array.length ; i++)
        {
            char c = array[i];
            char next = i+1 < array.length ? array[i+1] : 0;
            if(c == '$' && next == '{')
            {
                stack.push(new Block(i));
            }
            else if(c == '}')
            {
                Block peek = stack.pop();
                
                peek.buffer.append(c);
                String key = peek.buffer.toString();
                key = key.substring(1, key.length() - 1);
                String value = interpolate(key, map);
                
                peek = stack.peek();
                if(peek != null)
                {
                    peek.buffer.append(value);
                }
            }
            else
            {
                stack.peek().buffer.append(c);
            }
        }
        return stack.peek().buffer.toString();
    }

}

class Block
{

    int start;
    
    StringBuffer buffer = new StringBuffer();

    
    
    public Block(int start)
    {
        this.start = start;
    }

    @Override
    public String toString()
    {
        String value = buffer.toString(); 
        return "("+start+") "+ value;
    }
}
