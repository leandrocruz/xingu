package xingu.codec.impl;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.codec.Codec;
import xingu.type.TypeFactory;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public abstract class CodecSupport
    implements Codec, Configurable
{
    @Inject
    protected TypeFactory typeFactory;

    protected OutputMode output;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        String mode = conf.getChild("output").getAttribute("mode", "XML");
        output = OutputMode.valueOf(mode);
    }

    @Override
    public <T> T decode(String text, Class<? extends T> clazz)
    	throws Exception
    {
        throw new NotImplementedYet("TODO");
    }
}
