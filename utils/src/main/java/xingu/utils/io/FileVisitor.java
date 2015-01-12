package xingu.utils.io;

import java.io.File;

public interface FileVisitor
{

	void visit(File file)
		throws Exception;

}
