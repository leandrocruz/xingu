package xingu.maze;

public interface Config
{
    String mainClass();

	String pidFile();
	
    Domain domain(String name);

    Domain defaultDomain();
}