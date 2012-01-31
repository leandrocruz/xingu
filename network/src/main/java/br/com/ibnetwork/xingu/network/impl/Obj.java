package br.com.ibnetwork.xingu.network.impl;

import org.apache.commons.lang.ObjectUtils;

public class Obj
{
    public static String id(Object object)
    {
        String s = ObjectUtils.identityToString(object);
        return s.substring(object.getClass().getName().length());
    }
}
