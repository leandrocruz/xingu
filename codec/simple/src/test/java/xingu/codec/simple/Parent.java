package xingu.codec.simple;

import java.util.ArrayList;
import java.util.List;

//@Root
public class Parent
{
	int i;
	
	String s;
	
	Child c;

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
