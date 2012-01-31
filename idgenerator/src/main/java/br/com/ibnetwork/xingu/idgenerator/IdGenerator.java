package br.com.ibnetwork.xingu.idgenerator;

public interface IdGenerator
{
	Generator<?> generator()
		throws GeneratorException;

	Generator<?> generator(String id)
		throws GeneratorException;
}
