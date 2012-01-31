package br.com.ibnetwork.xingu.factory.test.impl;

import br.com.ibnetwork.xingu.factory.test.My;

public class MyImpl
    implements My
{
    private String value;

    public MyImpl()
    {
        this.value= "default";
    }

    public MyImpl(String value)
    {
        this.value= value;
    }
    
    public String getValue()
    {
        return value;
    }

}
