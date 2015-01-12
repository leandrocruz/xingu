package xingu.container;

import org.apache.avalon.framework.configuration.Configuration;

public interface Binding<T>
{
    Class<T> role();

    <E extends T> Binding<T> to(E impl);
    
    <E extends T> Binding<T> to(Class<E> impl);
    
    Binding<T> asDefault();

    T impl();

    <E extends T> Class<E> implClass();

    boolean isReady();

    void isReady(boolean ready);
    
    Binding<T> with(Configuration conf);
    
    Configuration configuration();
    
    boolean isDefault();
}
