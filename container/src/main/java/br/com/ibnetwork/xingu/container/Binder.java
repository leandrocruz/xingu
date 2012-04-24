package br.com.ibnetwork.xingu.container;

import java.util.List;

public interface Binder
{
    <T> Binding<T> bind(Class<T> role);
    
    <T> Binding<T> bind(Class<T> role, String key);

    <T> Binding<T> get(Class<T> role);
    
    <T> Binding<T> get(Class<T> role, String key);
    
    List<Class<?>> getRoles();
}
