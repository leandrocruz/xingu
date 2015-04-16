package xingu.type;

import java.util.List;

import xingu.type.transformer.Transformer;

public interface Field
{
	String getColumn();

	String getName();

	void add(Transformer transformer);

	List<Transformer> getFilters();
}
