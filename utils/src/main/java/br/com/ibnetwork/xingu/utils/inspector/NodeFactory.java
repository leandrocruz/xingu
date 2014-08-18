package br.com.ibnetwork.xingu.utils.inspector;

public interface NodeFactory
{
	/*
	 * 
	 * Stores results until a new object is created.
	 * Used for objects without empty constructors
	 * 
	 */
	Object create();
}
