package br.com.ibnetwork.xingu.idgenerator;

public class GeneratorException 
	extends RuntimeException
{
    public GeneratorException()
    {
        super();
    }

    public GeneratorException(String message)
    {
        super(message);
    }

    public GeneratorException(Throwable t)
    {
        super(t);
    }

    public GeneratorException(String message, Throwable t)
    {
        super(message, t);
    }
}
