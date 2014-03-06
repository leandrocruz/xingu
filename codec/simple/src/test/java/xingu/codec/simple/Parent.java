package xingu.codec.simple;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

//@Root
public class Parent
{
	@Attribute
	int i;
	
	@Element
	String s;
	
	@Element(name="filho")
	Child c;

	@ElementList(name="filhos")
	List<Child> children = new ArrayList();
	
	public Parent()
	{}
	
	public Parent(int i, String s, Child c)
	{
		this.i = i;
		this.s = s;
		this.c = c;
	}
}
