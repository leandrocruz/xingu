package xingu.codec.impl;

public class CodecForTesting
    extends CodecSupport
{

    @Override
    public Object decode(String text)
    {
        return text;
    }

    @Override
    public String encode(Object object)
    {
        return object.toString();
    }

}
