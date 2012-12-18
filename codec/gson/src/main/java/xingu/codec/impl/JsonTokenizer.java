package xingu.codec.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class JsonTokenizer
{
    static boolean insideDoubleQuotes = false;
    static boolean insideObject = false;

    static char separator = ',';
    static char objectStart = '{';
    static char objectEnd = '}';
    static char quote = '"';
    static char escape = '\\';

    private ParserState state;
    
    public String[] parse(String s)
    {
        state = new InitialState();
        Context ctx = new Context();
        char[] array = s.toCharArray();
        
        for (int i = 0; i < array.length; i++)
        {
            char c = array[i];
            if(i > 0)
            {
                ctx.previous = array[i-1]; 
            }
            state = state.consume(ctx, c);
        }
        return ctx.parts();
    }
}

class Context
{
    public char previous = '\0';

    private List<String> parts = new ArrayList<String>();
    
    public void add(String part)
    {
        if(!StringUtils.isEmpty(part))
        {
            parts.add(part);
        }
    }

    public String[] parts()
    {
        return parts.toArray(new String[]{});
    }
}

abstract class ParserState
{
    protected ParserState parent;
    
    protected StringBuffer sb = new StringBuffer();
    
    public ParserState(ParserState lastState)
    {
        this.parent = lastState;
    }

    public abstract ParserState consume(Context ctx, char c);
    
    protected void append(char c)
    {
        sb.append(c);
    }
}

class InitialState
    extends ParserState
{

    public InitialState()
    {
        super(null);
    }

    @Override
    public ParserState consume(Context ctx, char c)
    {
        if(c == JsonTokenizer.objectStart)
        {
            ParserState next = new InsideObject(this); 
            next.append(c);
            return next;
        }
        if(c == JsonTokenizer.objectEnd)
        {
            
        }
        if(c == JsonTokenizer.separator)
        {
            
        }
        return this;
    }
}

class InsideObject
    extends ParserState
{
    public InsideObject(ParserState lastState)
    {
        super(lastState);
    }

    @Override
    public ParserState consume(Context ctx, char c)
    {
        append(c);
        if(c == JsonTokenizer.objectStart)
        {
            return new InsideObject(this);
        }
        if(c == JsonTokenizer.objectEnd)
        {
            ctx.add(sb.toString());
            return parent;
        }
        if(c == JsonTokenizer.quote)
        {
            return new InsideDoubleQuotes(this);
            
        }
        return this;
    }

    @Override
    protected void append(char c)
    {
        if(parent instanceof InsideObject)
        {
            parent.append(c);
        }
        else
        {
            super.append(c);
        }
    }
}

class InsideDoubleQuotes
    extends ParserState
{
    public InsideDoubleQuotes(ParserState lastState)
    {
        super(lastState);
    }

    @Override
    public ParserState consume(Context ctx, char c)
    {
        append(c);
        if(ctx.previous != JsonTokenizer.escape && c == JsonTokenizer.quote)
        {
            return parent;
        }
        return this;
    }

    @Override
    protected void append(char c)
    {
        parent.append(c);
    }
}