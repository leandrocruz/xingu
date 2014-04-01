package br.com.ibnetwork.xingu.utils.inspector;

public interface ObjectInspector
{
	void visit(ObjectVisitor<?> visitor);
}
