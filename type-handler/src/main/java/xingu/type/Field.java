package xingu.type;

import java.util.List;

public interface Field
{
	String getColumn();

	String getName();

	void add(Transformer transformer);

	List<Transformer> getFilters();
}
