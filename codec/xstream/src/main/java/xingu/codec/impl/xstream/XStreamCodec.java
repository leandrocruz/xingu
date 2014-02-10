package xingu.codec.impl.xstream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.codec.CodecObject;
import xingu.codec.impl.CodecSupport;
import xingu.codec.impl.OutputMode;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.Injector;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class XStreamCodec
    extends CodecSupport
    implements Initializable
{

    @Inject
    private Factory factory;
    
    @Inject
    private Injector injector;

    private String mode;
    
    private XStream xstream;
    
    private Charset charset;
    
    private ArrayList<SingleValueConverter> converters = new ArrayList<SingleValueConverter>();
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        super.configure(conf);
        Configuration xstreamconf = conf.getChild("xstream");
        mode = xstreamconf.getAttribute("mode", "ID_REFERENCES");
        String charsetName = xstreamconf.getAttribute("charset", "utf-8");
        logger.debug("Using charset {}", charsetName);
        charset = Charset.forName(charsetName);
        
        Configuration[] convertersConf = conf.getChildren("converter");
        for(Configuration c : convertersConf)
        {
        	String convName = c.getAttribute("class", null);
        	if(convName != null)
        	{
        		Class<?>             clazz     = ObjectUtils.loadClass(convName);
    			SingleValueConverter converter = (SingleValueConverter) factory.create(clazz);
    			converters.add(converter);
        	}
        }
    }
    
    @Override
    public void initialize() 
        throws Exception
    {
        if(output.equals(OutputMode.JSON))
        {
            xstream = new XStream(new JettisonMappedXmlDriver());
        }
        else
        {
            xstream = new XStream();
        }

        int mode = xstreamMode(this.mode);
        if(mode > 0)
        {
            xstream.setMode(mode);
        }
        
        for(SingleValueConverter converter : converters)
        {
            xstream.registerConverter(converter, XStream.PRIORITY_VERY_HIGH);
        }

        //aliases
        Set<String> types = typeFactory.types();
        for (String type : types)
        {
            Class<?> clazz = typeFactory.classByType(type);
            logger.debug("type alias: "+type+" -> "+clazz);
            xstream.alias(type, clazz);
        }
    }

    private int xstreamMode(String mode)
    {
        if("NO_REFERENCES".equals(mode))
        {
            return XStream.NO_REFERENCES;
        }
        if("ID_REFERENCES".equals(mode))
        {
            return XStream.ID_REFERENCES;
        }
        if("XPATH_RELATIVE_REFERENCES".equals(mode))
        {
            return XStream.XPATH_RELATIVE_REFERENCES;
        }
        if("XPATH_ABSOLUTE_REFERENCES".equals(mode))
        {
            return XStream.XPATH_ABSOLUTE_REFERENCES;
        }
        return -1;
    }

    @Override
    public Object decode(String json)
    {
        logger.debug(">> decode: {}", json);
        ByteArrayInputStream bytes = new ByteArrayInputStream(json.getBytes());
        Reader reader = new InputStreamReader(bytes, charset);
        Object result = null;
        try
        {
            result = xstream.fromXML(reader);
            injector.injectDependencies(result);
        }
        catch(Throwable t)
        {
            logger.error("Error decoding message", t);
        }
        if(result instanceof CodecObject)
        {
            ((CodecObject) result).afterDecode();
        }
        return result;
    }

    @Override
    public String encode(Object object)
    {
        if(object instanceof CodecObject)
        {
            ((CodecObject) object).beforeEncode();
        }
    	xstream.processAnnotations(object.getClass());    	
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(bytes, charset);
        try
        {
            xstream.toXML(object, writer);
        }
        catch(Throwable t)
        {
            logger.error("Error encoding message", t);
        }
        String json = new String(bytes.toByteArray(), charset);
        logger.debug("<< encode: {}", json);
        return json;
    }
}
