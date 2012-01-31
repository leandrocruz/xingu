package net.kidux.unicorn.commons.codec;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kidux.unicorn.commons.signal.Signal;
import net.kidux.unicorn.commons.signal.SignalSupport;
import net.kidux.unicorn.commons.signal.global.Ping;

import org.junit.Ignore;
import org.junit.Test;

import xingu.codec.Codec;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class CodecTest
	extends XinguTestCase
{
	@Inject("xstream")
    private Codec xstream;

	@Inject("gson")
    private Codec gson;

    @Override
	protected String getContainerFile() 
    {
    	return "pulga-commons.xml";
    }

	@Test
    public void testSimpleObjectSerialization()
    {
        Signal signal = new Ping(1, "@#$", null);
        
        String json1 = xstream.encode(signal);
        Ping obj1 = (Ping) xstream.decode(json1);
        
        String json2 = gson.encode(signal);
        Ping obj2 = (Ping) gson.decode(json2);
        json1 = json1.replace("America\\/Sao_Paulo", "America/Sao_Paulo"); //hack
        json2 = json2.replace("global.Ping", "net.kidux.unicorn.commons.signal.global.Ping");
        assertEquals(json1, json2);
        assertEquals(obj1.id(), obj2.id());
        assertEquals(obj1.sessionId(), obj2.sessionId());
        assertEquals(obj1.applicationId(), obj2.applicationId());
        assertEquals(obj1.uTime(), obj2.uTime());
    }

	@Test
    public void testListSerialization()
    {
        Signal signal = new MySignal(new String[]{"a","b","c"});
        
        String json1 = xstream.encode(signal);
        MySignal obj1 = (MySignal) xstream.decode(json1);

        String json2 = gson.encode(signal);
        MySignal obj2 = (MySignal) gson.decode(json2);
        
        assertEquals(obj1.list().size(), obj2.list().size());
        assertEquals(obj1.array().length, obj2.array().length);
    }

	@SuppressWarnings("unchecked")
    @Test
	public void testCollection()
	    throws Exception
	{
	    List<Object> list = new ArrayList<Object>();
        list.add(new SomeSignal("ali"));
        list.add(new SomeSignal("babá"));
        String json = gson.encode(list);
        List<Object> list2 = (List<Object>) gson.decode(json);
        assertEquals(list, list2);
	}
	
	@Test
    public void testEmptyCollection()
	    throws Exception
	{
	    List<Object> list = new ArrayList<Object>();
        String json = gson.encode(list);
        assertEquals("{\"java.util.ArrayList\":[]}", json);
    }

	@Test
	@Ignore
    public void testPerformance()
    {
        long start1 = System.currentTimeMillis();
        exec(xstream);
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        exec(gson);
        long end2 = System.currentTimeMillis();
        
        System.out.println("XStream: "+(end1 - start1));
        System.out.println("GSon: "+(end2 - start2));
    }

	
	private void exec(Codec codec) 
	{
		Signal list = new MySignal(new String[]{"a","b","c"});
		Signal log = new Ping(1, "99", null);
		String json;
		@SuppressWarnings("unused")
        Object obj;
		for(int i=0 ; i<100 ; i++)
		{
			json = codec.encode(list);
			obj = codec.decode(json);
			json = codec.encode(log);
			obj = codec.decode(json);
		}
	}
}

@SuppressWarnings("serial")
class SomeSignal
    extends SignalSupport
{
    @SuppressWarnings("unused")
    private String some;
    
    SomeSignal()
    {}
    
    public SomeSignal(String some)
    {
        this.some = some;
        this.sessionId = "#@$";
        this.applicationId = "app";
    }
}

@SuppressWarnings("serial")
class MySignal
	extends SignalSupport
{
	private List<String> listOfString;
	
	private String[] arrayOfString;
	
	public MySignal()
	{}
	
	public MySignal(String[] data)
	{
		this.arrayOfString = data;
		this.listOfString = Arrays.asList(data);
	}

	public String[] array() 
	{
		return arrayOfString;
	}

	public List<String> list() 
	{
		return listOfString;
	}
}