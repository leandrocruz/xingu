package xingu.inspector;

public interface ObjectInspector
{
	void visit(ObjectVisitor<?> visitor);
}
