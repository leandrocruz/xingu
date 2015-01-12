package xingu.utils.lang;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.Test;

import xingu.lang.Constructors;

public class ConstructorsTest
{
    @Test
    public void testFindConstructor()
        throws Exception
    {
        Constructor<?> constructor;
        constructor = Constructors.findConstructor(SomeObject.class, (Object[]) null);
        testValue(0, constructor, (Object[]) null);

        constructor = Constructors.findConstructor(SomeObject.class, 1);
        testValue(1, constructor, 1);

        constructor = Constructors.findConstructor(SomeObject.class, "10");
        testValue(10, constructor, "10");


        constructor = Constructors.findConstructor(SomeObject.class, new SomeObject(100));
        testValue(100, constructor, new SomeObject(100));
        

        constructor = Constructors.findConstructor(SomeObject.class, new Other(1000));
        testValue(1000, constructor, new Other(1000));
    }

    private void testValue(int i, Constructor<?> constructor, Object... params) 
        throws Exception
    {
        SomeObject obj = (SomeObject) constructor.newInstance(params);
        assertEquals(i, obj.i);
    }
}

class SomeObject
{
    int i;
    
    public SomeObject()
    {}

    public SomeObject(int i)
    {
        this.i = i;
    }

    public SomeObject(Integer i)
    {
        this.i = i;
    }

    public SomeObject(String i)
    {
        this.i = Integer.parseInt(i);
    }

    public SomeObject(SomeObject other)
    {
        this.i = other.i;
    }
}

class Other
    extends SomeObject
{
    public Other(int number)
    {
        super(number);
    }
}