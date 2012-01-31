package xingu.codec.impl.gson;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CollectionSerializer
    implements TypeAdapter, JsonSerializer<Collection<?>>
{
    @Override
    public Type type()
    {
        return Collection.class;
    }
    
    @Override
    public JsonElement serialize(Collection<?> collection, Type type, JsonSerializationContext context)
    {
        //TODO may be useful to turn this array into a 'typed object' so that it can be deserialized
        //to the same concrete instance of collection being serialized here
        JsonArray array = new JsonArray();
        for (Object item : collection)
        {
            JsonElement element;
            if (item.getClass().isPrimitive()
                || item instanceof String
                || item instanceof Map<?, ?>)
            {
                element = context.serialize(item);
            }
            else
            {
                element = wrapTypedObject(item, context);
            }
            array.add(element);
        }
        return array;
    }

    private JsonElement wrapTypedObject(Object object, JsonSerializationContext context)
    {
        String typeName = object.getClass().getName();
        JsonElement value = context.serialize(object);
        JsonObject element = new JsonObject();
        element.add(typeName, value);
        return element;
    }
}