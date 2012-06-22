package br.com.ibnetwork.xingu.utils.clone;

public interface FastCloner<T>
{
	T clone(CloningContext ctx, T original, Cloner cloner);
}
