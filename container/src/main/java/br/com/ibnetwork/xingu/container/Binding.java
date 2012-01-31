package br.com.ibnetwork.xingu.container;

public interface Binding<T>
{
    Class<T> role();

    <E extends T> void to(E impl);
    
    <E extends T> void to(Class<E> impl);

    T impl();
    
    <E extends T> Class<E> implClass();

    boolean isReady();

    void isReady(boolean ready);

}
