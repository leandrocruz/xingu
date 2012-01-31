package br.com.ibnetwork.xingu.container;

public interface Binder
{
    <T> Binding<T> bind(Class<T> role);
    
    <T> Binding<T> bind(Class<T> role, String key);

    <T> Binding<T> get(Class<T> role);
    
    <T> Binding<T> get(Class<T> role, String key);
}
