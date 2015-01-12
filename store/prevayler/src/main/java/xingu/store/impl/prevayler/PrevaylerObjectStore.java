package xingu.store.impl.prevayler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Transaction;
import org.prevayler.foundation.serialization.XStreamSerializer;
import org.prevayler.implementation.PrevaylerDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Environment;
import xingu.container.Inject;
import xingu.container.Injector;
import xingu.factory.Factory;
import xingu.store.Initializable;
import xingu.store.PersistentBean;
import xingu.store.SnapshotListener;
import xingu.store.StoreException;
import xingu.store.impl.BadObjectStore;
import xingu.store.impl.prevayler.snapshot.SimpleSnapshooter;
import xingu.utils.ObjectUtils;
import xingu.utils.TimeUtils;

public class PrevaylerObjectStore
	extends BadObjectStore
    implements SnapshotListener, Configurable, Startable
{
    @Inject
    private Environment env; 
    
    @Inject
    private Injector injector;

    @Inject
    private Factory factory;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final long KILOBYTE = 1024;

    private Prevayler prevayler;

	private Cache cache;
	
	private String prevalenceDirectory;

    private boolean clearPrevalenceDirectory;
	
    private long journalFileSizeThreshold; //in kilobytes
    
    private long snapshotInterval; //in minutes
    
    private Timer snapshotTimer;

    private boolean enableFoodTaster;
    
    private boolean takeSnapshotOnStart;
	
    private String snapshooterClass;
    
    private Configuration snapshooterConf;
    
    private boolean clearPrevalenceDirectoryOnError;

    private volatile boolean busyTakingSnapshot;
    
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        conf = conf.getChild("prevayler");
        prevalenceDirectory = env.replaceVars(conf.getAttribute("prevalenceDirectory","/tmp"));
        clearPrevalenceDirectory = conf.getAttributeAsBoolean("clearPrevalenceDirectory", false);
        String interval = conf.getAttribute("snapshotInterval", "60s");
        snapshotInterval = TimeUtils.toMillis(interval);
        journalFileSizeThreshold = KILOBYTE * conf.getAttributeAsLong("journalFileSizeThreshold", 0);
        enableFoodTaster = conf.getAttributeAsBoolean("enableFoodTaster", false);
        takeSnapshotOnStart = conf.getAttributeAsBoolean("takeSnapshotOnStart", true);
        clearPrevalenceDirectoryOnError = conf.getAttributeAsBoolean("clearPrevalenceDirectoryOnError", false);
        snapshooterConf = conf.getChild("snapshooter");
        snapshooterClass = snapshooterConf.getAttribute("class", SimpleSnapshooter.class.getName());
    }
    
    public void start() 
        throws Exception
    {
        logger.info("Starting prevayler");
        if(clearPrevalenceDirectory)
        {
            clearPrevalenceDirectory(false);
        }
        long start = System.currentTimeMillis();
        try
        {
            prevayler = createPrevayler();
        }
        catch(Throwable t)
        {
            //serialization error?
            if(clearPrevalenceDirectoryOnError)
            {
                logger.error("Error creating prevayler. Prevayler directory '"+prevalenceDirectory+"' will be erased", t);
                clearPrevalenceDirectory(true);
                prevayler = createPrevayler();
            }
            else
            {
                logger.error("Error creating prevayler", t);
            }
        }
        cache = (Cache) prevayler.prevalentSystem();
        logger.info("Prevayler started from '{}'. Cache count is '{}'. Took '{}'", 
                new Object[]{prevalenceDirectory, cache.size(), TimeUtils.toSeconds(System.currentTimeMillis() - start)});
        logger.info("Latest snapshot file is '{}'", new PrevaylerDirectory(prevalenceDirectory).latestSnapshot());
        scheduleSnapshots();
    }

    public void stop() 
        throws Exception
    {
        if (snapshotTimer != null)
        {
            snapshotTimer.cancel();
        }
        prevayler.close();
    }
    
    private void scheduleSnapshots()
    {
        if (snapshotInterval > 0)
        {
            snapshotTimer             = new Timer("Prevayler Snapshooter Timer", true);
            Class<?>      clazz       = ObjectUtils.loadClass(snapshooterClass);
            TimerTask     snapshooter = (TimerTask) factory.create(clazz, this, prevayler, prevalenceDirectory, snapshooterConf);
            long          deplay      = snapshotInterval;
            if(takeSnapshotOnStart)
            {
                deplay = 300;
            }
            snapshotTimer.scheduleAtFixedRate(snapshooter, deplay, snapshotInterval);
            logger.info("Snapshots are scheduled for every '{}'", TimeUtils.toMinutes(snapshotInterval));
        }
        else
        {
            logger.info("Snapshots are disabled");
        }
    }

    private void clearPrevalenceDirectory(boolean moveFiles)
        throws IOException
    {
        File dir = new File(prevalenceDirectory);
        if(dir.exists())
        {
            if(moveFiles)
            {
                FileFilter filesOnly = FileFilterUtils.fileFileFilter();
                File[] files = dir.listFiles(filesOnly);
                if(files != null)
                {
                    File destDir = new File(prevalenceDirectory, "deleted-"+System.currentTimeMillis());
                    for(File file : files)
                    {
                        FileUtils.moveToDirectory(file, destDir, true);
                    }
                }
            }
            else
            {
                FileUtils.cleanDirectory(dir);
            }
        }
    }

    private Prevayler createPrevayler()
        throws IOException, ClassNotFoundException
    {
        PrevaylerFactory factory = new PrevaylerFactory();
        XStreamSerializer serializer = new XStreamSerializer("UTF-8");
		factory.configureJournalSerializer(serializer);
		factory.configureSnapshotSerializer(serializer);
		factory.configurePrevalentSystem((Serializable) new Cache());
		factory.configurePrevalenceDirectory(prevalenceDirectory);
		factory.configureJournalFileSizeThreshold(journalFileSizeThreshold);
		factory.configureTransactionFiltering(enableFoodTaster);
		return factory.create();
    }
	
    
    public Map<Class, List<PersistentBean>> cache()
    {
    	return cache._cache;
    }
    
	public synchronized <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> clazz)
    	throws StoreException
    {
		List<POJO> list = cache.get(clazz);
		for (POJO pojo : list)
        {
            init(pojo);
        }
		return list; 
    }

	public <POJO extends PersistentBean> POJO getById(Class<POJO> clazz, long id)
		throws StoreException
	{
		List<POJO> list = cache.get(clazz);
		if(list == null)
		{
			return null;
		}
		for (POJO pojo : list)
		{
			if(pojo.getId() == id)
			{
			    init(pojo);
				return pojo;
			}
		}
		return null;
	}

    private <POJO> void init(POJO pojo)
    {
        try
        {
            injector.injectDependencies(pojo);
            if(pojo instanceof Initializable)
            {
                ((Initializable) pojo).initialize();
            }
        }
        catch (Exception e)
        {
            throw new StoreException("Error injecting dependencies",e);
        }
    }

	public synchronized <POJO extends PersistentBean> int delete(POJO pojo)
	    throws StoreException
	{
	    RemoveFromCacheTransaction transaction = new RemoveFromCacheTransaction(pojo);
	    prevayler.execute(transaction);
		return 0;
	}
	
	public synchronized <POJO extends PersistentBean> int store(POJO pojo)
    	throws StoreException
    {
	    Transaction transaction;
		if(pojo.getId() <= 0)
		{
		    long nextId = nextId(pojo);
			pojo.setId(nextId);
			transaction = new AddToCacheTransaction(pojo);
		}
		else
		{
		    transaction = new UpdateCacheTransaction(pojo);
		}
		prevayler.execute(transaction);
		return 0;
    }

	/*
	 * TODO: lock id generation for all objects of this class
	 */
	private synchronized <POJO extends PersistentBean> long nextId(POJO pojo)
    {
		Class clazz = pojo.getClass();
		List<POJO> all = getAll(clazz);
		if(all == null)
		{
		    return 1;
		}
		long nextId = 0;
		for (POJO tmp : all)
		{
		    if(tmp.getId() > nextId)
		    {
		        nextId = tmp.getId();
		    }
		}
		return nextId + 1;
    }

	public <POJO extends PersistentBean> int delete(Class<POJO> clazz, POJO pojo)
	    throws StoreException
	{
		return delete(pojo);
	}

	public <POJO extends PersistentBean> int store(Class<POJO> clazz, POJO pojo)
	    throws StoreException
	{
		return store(pojo);
	}

    @Override
    public void clear() 
        throws StoreException
    {
        try
        {
            prevayler.close();
            prevayler = createPrevayler();
            this.cache.clear();
            clearPrevalenceDirectory(false);
            this.prevayler.takeSnapshot();
        }
        catch (Exception e)
        {
            throw new StoreException("Error cleaning preayler", e);
        }
    }

    @Override
    public void takeSnapshot() 
        throws StoreException
    {
        if(busyTakingSnapshot)
        {
            logger.warn("Already busy taking snapshot. Come back later...");
        }
        else
        {
            //TODO: we must notify the snapshotTimer/snapshooter that we are taking a Snapshot. Or use a lock
            try
            {
                prevayler.takeSnapshot();
            }
            catch (IOException e)
            {
                throw new StoreException("Error creating snapshot", e);
            }
        }
    }

    @Override
    public void beforeSnapshot()
    {
        this.busyTakingSnapshot = true;
    }

    @Override
    public void snapshotFailed(Exception e)
    {}

    @Override
    public void afterSnapshot()
    {
        this.busyTakingSnapshot = false;
    }
}

class Cache<POJO>
	implements Serializable
{
	Map<Class, List<PersistentBean>> _cache = new HashMap<Class, List<PersistentBean>>();

	public long size()
    {
		long count = 0;
	    Set<Class> keys = _cache.keySet();
	    for (Class clazz : keys)
        {
	    	count +=_cache.get(clazz).size();
        }
		return count;
    }

	public void clear()
    {
        _cache.clear();
    }

    public List get(Class clazz)
    {
	    Set<Class> keys = _cache.keySet();
	    List<PersistentBean> result = new ArrayList<PersistentBean>();
 	    
	    //search for parent classes
	    for (Class entry : keys)
        {
            if(clazz.isAssignableFrom(entry))
            {
                List tmp = _cache.get(entry);
                if(tmp != null)
                {
                    result.addAll(tmp);
                }
            }
        }
	    return result;
    }

	public void put(Class clazz, List<PersistentBean> beans)
    {
		_cache.put(clazz, beans);
    }

    public void remove(PersistentBean bean)
    {
        List<PersistentBean> all = _cache.get(bean.getClass());
        long id = bean.getId();
        if(all != null)
        {
            Iterator<PersistentBean> it = all.iterator();
            while (it.hasNext())
            {
                PersistentBean tmp = it.next();
                if(tmp.getId() == id)
                {
                    it.remove();
                }
            }
        }
    }

    public void add(PersistentBean bean)
    {
        Class clazz = bean.getClass();
        List<PersistentBean> all = _cache.get(clazz);
        if(all == null)
        {
            all = new ArrayList<PersistentBean>();
            _cache.put(clazz, all);
        }
        all.add(bean);
    }

    @Override
    public String toString()
    {
        return "PrevaylerCache ("+size()+")";
    }
    
    
}