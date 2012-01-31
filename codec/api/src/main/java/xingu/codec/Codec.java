package xingu.codec;

public interface Codec
{
    Object decode(String text);
    
    <T> T decode(String text, Class<? extends T> clazz);
   
    String encode(Object object);
}
