package xingu.node.commons.protocol;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.CharsetUtil;

import xingu.codec.Codec;
import xingu.netty.protocol.FrameBasedMessageEncoder;
import xingu.node.commons.universe.Universe;
import xingu.node.commons.universe.Universes;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.FieldUtils;

public class PojoEncoder
	extends FrameBasedMessageEncoder
{
    @Inject("proto")
    private Codec codec;
    
    @Inject
    private Universes universes;

	@Override
	protected byte[] toByteArray(Channel channel, Object obj, int type)
		throws Exception
	{
        Class<?>    clazz  = obj.getClass();
		List<Field> fields = FieldUtils.getAllFields(clazz);
        for (Field field : fields)
        {
            processInject(obj, field);
            processToString(obj, field);
            processToMap(obj, field);
        }

        Universe universe = universes.universeFor(obj);
    	String   encoded  = universe.id() + "@" + codec.encode(obj);

    	return encoded.getBytes(CharsetUtil.UTF_8);
	}

	private void processToMap(Object obj, Field field)
		throws Exception
	{
		Class<?> clazz = obj.getClass();
		ToMap    toMap = field.getAnnotation(ToMap.class);
		if(toMap != null)
		{
			String storeTo = toMap.value();
			Field  target  = FieldUtils.getField(clazz, storeTo);
			Object value   = FieldUtils.valueFrom(field, obj);
			Map    map     = BeanUtils.describe(value);
			FieldUtils.set(target, obj, map);
			FieldUtils.set(field,  obj, null);
		}
	}

	private void processToString(Object obj, Field field)
		throws Exception
	{
		Class<?> clazz    = obj.getClass();
		ToString toString = field.getAnnotation(ToString.class);
		if(toString != null)
		{
			String storeTo = toString.value();
			Field  target  = FieldUtils.getField(clazz, storeTo);
			Object value   = FieldUtils.valueFrom(field, obj);
			String text    = codec.encode(value);
			FieldUtils.set(target, obj, text);
			FieldUtils.set(field,  obj, null);
		}
	}

	private void processInject(Object obj, Field field)
	{
		Inject inject = field.getAnnotation(Inject.class);
		if(inject != null)
		{
			FieldUtils.set(field, obj, null);
		}
	}
}