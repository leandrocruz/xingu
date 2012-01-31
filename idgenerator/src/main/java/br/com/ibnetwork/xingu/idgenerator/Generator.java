package br.com.ibnetwork.xingu.idgenerator;

public interface Generator<T>
{
	T state();
	
    T next()
        throws GeneratorException;
}
