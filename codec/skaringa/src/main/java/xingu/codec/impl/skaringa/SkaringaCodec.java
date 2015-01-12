package xingu.codec.impl.skaringa;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.codec.impl.CodecSupport;
import xingu.lang.NotImplementedYet;

import com.skaringa.javaxml.DeserializerException;
import com.skaringa.javaxml.ObjectTransformer;
import com.skaringa.javaxml.ObjectTransformerFactory;
import com.skaringa.javaxml.SerializerException;


public class SkaringaCodec
    extends CodecSupport
    implements Initializable
{
    private ObjectTransformer transformer;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void initialize()
        throws Exception
    {
        transformer = ObjectTransformerFactory.getInstance().getImplementation();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T decode(String text, Class<? extends T> clazz)
    {
        InputStream is = new ByteArrayInputStream(text.getBytes());
        Object result = null;
        try
        {
            result = transformer.deserializeFromJson(is, clazz);
        }
        catch (DeserializerException e)
        {
            logger.warn("Error deserialing text '"+text+"' into instance of '"+clazz+"'", e);
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
        return (T) result;
    }

    @Override
    public Object decode(String text)
    {
        throw new NotImplementedYet("TODO");
    }

    @Override
    public String encode(Object object)
    {
        try
        {
            return transformer.serializeToJsonString(object);
        }
        catch (SerializerException e)
        {
            logger.warn("Error serializing object '"+object+"'");
        }
        return null;
    }
}
