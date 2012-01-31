package br.com.ibnetwork.xingu.store.impl.db4o;

import java.io.File;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.store.ObjectStore;
import br.com.ibnetwork.xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.store.StoreException;
import br.com.ibnetwork.xingu.utils.FileBackup;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;

public class Db4oObjectStore
    implements ObjectStore, Configurable, Initializable
{
    private ObjectContainer db;

    private String dbFile;
    
    private boolean clearDatabaseOnStartup;
    
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        dbFile = conf.getChild("db4o").getAttribute("file","/var/run/db4o/database");
        clearDatabaseOnStartup = conf.getChild("db4o").getAttributeAsBoolean("clearDatabaseOnStartup",true);
    }

    @Override
    public void initialize() 
        throws Exception
    {
        if(clearDatabaseOnStartup)
        {
            File bck = new FileBackup(dbFile).backup();
            if(bck != null)
            {
                FileUtils.writeStringToFile(new File(dbFile), "");
            }
        }
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration(); 
        db = Db4oEmbedded.openFile(config, dbFile);
    }

    @Override
    public <POJO extends PersistentBean> int delete(POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> int delete(Class<POJO> pojoClass, POJO pojo) 
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> pojoClass)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> POJO getById(final Class<POJO> pojoClass, final long id) 
        throws StoreException
    {
        ObjectSet<PersistentBean> result = db.query(new Predicate<PersistentBean>() {
            @Override
            public boolean match(PersistentBean bean)
            {
                return bean.getId() == id && pojoClass.equals(bean.getClass());
            }
        });
        
        int size = result.size(); 
        if(size == 1)
        {
            return (POJO) result.get(0);
        }
        if(size == 0)
        {
            return null;
        }
        
        throw new StoreException("Found "+size+" objects for "+pojoClass.getName()+" with id "+id);
    }

    @Override
    public <POJO extends PersistentBean> void insert(POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> void insert(Class<POJO> pojoClass, POJO pojo) 
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> int store(POJO pojo)
        throws StoreException
    {
        db.store(pojo);
        return 0;
    }

    @Override
    public <POJO extends PersistentBean> int store(Class<POJO> pojoClass, POJO pojo) 
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> int update(POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> int update(Class<POJO> pojoClass, POJO pojo) 
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public void clear() 
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public void takeSnapshot() 
        throws StoreException
    {
        throw new NotImplementedYet("TODO");
    }
}
