package br.com.ibnetwork.xingu.utils.io;

import java.io.File;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class SerialFileContainer
{
	private File				root;

	private FileNamer<Integer>	namer;    

    
    public SerialFileContainer(File root, FileNamer<Integer> namer)
    {
        this.root  = root;
		this.namer = namer;
    }

    public File next()
    {
    	int    idx  = lastIndex();
    	String name = namer.getName(idx + 1);
    	File   file = new File(root, name);
    	if(file.exists())
    	{
    		throw new NotImplementedYet();
    	}
    	return file;
    }
    
    private int lastIndex()
	{
    	int max = 0;
    	String[] files = root.list();
    	for(String name : files)
		{
    		int idx = namer.getParam(name);
    		max = idx > max ? idx : max;
		}
    	return max;
	}

    @Override
    public String toString()
    {
        return "SerialFileContainer @ "+root;
    }
}
