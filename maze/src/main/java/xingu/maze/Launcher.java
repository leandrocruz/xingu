package xingu.maze;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Map;

public class Launcher
{
    private Config config;
    
	private boolean withDebug;

	private final Args args;
    
    public Launcher(Args args, Config config)
    {
        this.args = args;
		this.config = config;
    	withDebug = args.has("debug");
    }

    public static void main(String[] arguments)
        throws Exception
    {
    	Args args = new Args(arguments);
        File currentDirectory = new File(".");
        if(args.has("debug")) System.out.println("[MAZE] Starting maze from: "+currentDirectory.getAbsolutePath());
        
        String fileName = Utils.configurationFileName();
        Config config = Utils.config(fileName);
        new Launcher(args, config).launch();
    }

    public void launch() 
        throws Exception
    {
    	ClassLoader cl = config.defaultDomain().classLoader();
        Thread.currentThread().setContextClassLoader(cl);
        
        String mainClass = config.mainClass();
        print("Running " + mainClass + " with " + args.size() + " args");

        Class<?> clazz = Class.forName(mainClass, false, cl);
        print(mainClass + " loaded from " + clazz.getClassLoader());    

        String pidFile = config.pidFile();
        if(pidFile != null)
        {
			writePidToFile(pidFile);
        }

        if(args.has("env"))
        {
        	printEnv();
        }
        
        Method main = Utils.getMain(clazz);
        main.invoke(clazz, new Object[]{args.toArray()});
    }

	private void printEnv()
	{
    	System.out.println("Environment is:");
    	Map<String, String> env = System.getenv();
    	for(String key : env.keySet())
    	{
    		System.out.println("\t"+key+" = " + env.get(key));
    	}
	}
	
	private void writePidToFile(String pidFile)
		throws Exception
	{
		File self = new File("/proc/self");
		if(self.exists())
		{
			String pid = self.getCanonicalFile().getName();
			print("Writing pid '"+pid+"' to file '"+pidFile+"'");
			File file = new File(pidFile);
			OutputStream os = new FileOutputStream(file);
			os.write(pid.getBytes());
			os.flush();
			os.close();
		}
	}

	private void print(String message)
	{
		print(message, false);
	}

	private void print(String message, boolean force)
	{
		if(withDebug || force)
		{
			System.out.println("[MAZE] " + message);
		}
	}

}