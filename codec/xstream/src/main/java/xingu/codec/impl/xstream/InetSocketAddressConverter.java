package xingu.codec.impl.xstream;

import java.net.InetSocketAddress;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * This converter prevents InetSocketAddress to be (de)serialized.
 */
public class InetSocketAddressConverter
    implements Converter
{
    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type)
    {
        return type.equals(InetSocketAddress.class);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
    {}

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
    {
        return null;
    }
}
