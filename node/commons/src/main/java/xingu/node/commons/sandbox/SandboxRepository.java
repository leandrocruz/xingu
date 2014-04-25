package xingu.node.commons.sandbox;

import java.util.List;

public interface SandboxRepository
{
	Sandbox download(String id)
		throws Exception;

	List<Sandbox> getAll();
}
