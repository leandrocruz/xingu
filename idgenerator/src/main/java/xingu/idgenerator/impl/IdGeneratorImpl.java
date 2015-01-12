package xingu.idgenerator.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.idgenerator.Generator;
import xingu.idgenerator.GeneratorException;
import xingu.idgenerator.IdGenerator;
import xingu.utils.ObjectUtils;

public class IdGeneratorImpl 
	implements IdGenerator, Configurable
{
    @Inject
    private Factory factory;

    private static final String DEFAULT = "default";
    
    private Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Generator<?>> registry;

    @Override
    public void configure(Configuration config) 
        throws ConfigurationException
    {
        Configuration[] generators = config.getChild("generators").getChildren("generator");
        registry = new HashMap<String, Generator<?>>(generators.length);
        for (Configuration conf : generators)
        {
            String   id        = conf.getAttribute("id", DEFAULT);
            String   className = conf.getAttribute("class");
            int      grabSize  = conf.getAttributeAsInteger("grabSize", 20);
            Class<?> clazz     = ObjectUtils.loadClass(className);
            Generator<?> gen = (Generator<?>) factory.create(clazz, id, grabSize);
            logger.info("Creating generator '{}'", className);
            registry.put(id, gen);
        }
    }

    @Override
    public Generator<?> generator() 
        throws GeneratorException
    {
        return generator(DEFAULT);
    }

    @Override
    public Generator<?> generator(String generatorId) 
        throws GeneratorException
    {
        return registry.get(generatorId);
    }
}
