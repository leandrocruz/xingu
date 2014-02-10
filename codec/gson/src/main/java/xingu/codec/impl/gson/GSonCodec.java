package xingu.codec.impl.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.codec.impl.CodecSupport;
import xingu.codec.impl.JsonTokenizer;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSonCodec
    extends CodecSupport
    implements Initializable
{
    @Inject
    private Factory factory;
    
    private Gson gson;
    
    private List<TypeAdapter> typeAdapters = new ArrayList<TypeAdapter>();
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        super.configure(conf);
        conf = conf.getChild("typeAdapters");
        for (Configuration adapterConf : conf.getChildren("adapter"))
        {
            String      className = adapterConf.getAttribute("class");
            Class<?>    clazz     = ObjectUtils.loadClass(className);
            TypeAdapter adapter   = (TypeAdapter) factory.create(clazz);
            typeAdapters.add(adapter);
        }
    }
    
    @Override
    public void initialize() 
        throws Exception
    {
    	GsonBuilder builder = new GsonBuilder();
    	for (TypeAdapter adapter : typeAdapters)
    	{
    	    Type type = adapter.type();
            builder.registerTypeAdapter(type, adapter);
    	}
        gson = builder.create();
    }
    
    @Override
    public Object decode(String json)
    {
        logger.debug(">> decode: {}", json);
    	int index = json.indexOf(":");
    	String type = json.substring(2, index - 1);
    	Class<?> clazz = typeFactory.classByType(type);
    	String attributes = json.substring(index + 1);
    	if(Collection.class.isAssignableFrom(clazz))
    	{
    	    Collection<Object> col = (Collection<Object>) ObjectUtils.getInstance(clazz);
    	    String[] parts = new JsonTokenizer().parse(attributes);
    	    for (String part : parts)
            {
    	        Object obj = decode(part);
    	        col.add(obj);
            }
    	    return col;
    	}
    	else
    	{
    	    Object result = gson.fromJson(attributes, clazz);
    	    return result;
    	}
    }

	@Override
    public String encode(Object object)
    {
        String json = gson.toJson(object);
        String typeName = typeFactory.typeOf(object);
        StringBuffer buffer = new StringBuffer();
        buffer.append("{\"").append(typeName).append("\":").append(json).append("}");
	    String result = buffer.toString();
        logger.debug("<< encode: {}", buffer);
        return result;
    }
}