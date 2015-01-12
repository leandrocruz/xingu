package xingu.lang;

import java.io.Serializable;

public interface WithId
	extends Serializable
{
	void setId(long id);
	
	long getId();
}
