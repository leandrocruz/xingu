package br.com.ibnetwork.xingu.container.components;

import xingu.container.Container;

public interface UsesAnnotations
{
    Container getContainer();
    
    Simple getD1();
    
    Simple getD2();

    //defined is super class
    Component getD3();
}
