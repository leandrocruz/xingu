package br.com.ibnetwork.xingu.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;

public class MessageQueueTest
    extends XinguTestCase
{
    @Inject
    private QueueManager mqm;
    
    @After
    public void cleanup()
    {
        mqm.clear();
    }
    
    @Test
    public void testFindQueue()
        throws Exception
    {
        String queueId = "some-queue-id";
        mqm.newQueue(queueId);
        MessageQueue q = mqm.queue(queueId);
        assertEquals(queueId, q.id());
        
        q = mqm.queue("ghost");
        assertNull(q);
    }

    @Test
    public void testDuplicateQueue()
        throws Exception
    {
        String queueId = "some-queue-id";
        mqm.newQueue(queueId);
        try
        {
            mqm.newQueue(queueId);
            fail("Should have thrown exception");
        }
        catch(QueueManagerException e)
        {
            //ignored
        }
    }
    
    @Test
    public void testCreateQueue()
        throws Exception
    {
        List<MessageQueue> list = mqm.list();
        assertEquals(0, list.size());
        
        //create new Queue
        String queueId = "some-queue-id";
        mqm.newQueue(queueId);
        list = mqm.list();
        assertEquals(1, list.size());
        MessageQueue q = list.get(0);
        assertEquals(0, q.count());
        assertEquals(queueId, q.id());

        //Search for Queue
        MessageQueue q2 = mqm.queue(queueId);
        assertSame(q, q2);
    }
    
    @Test
    public void testEnqueue()
        throws Exception
    {
        //create new Queue
        String queueId = "some-queue-id";
        MessageQueue q = mqm.newQueue(queueId);
        assertEquals(0, q.count());
        
        //add message to queue
        Message msg = new SampleMessage();
        assertEquals(0, msg.id());
        q.enqueue(msg);
        assertNotNull(msg.id());
        assertEquals(1, q.count());
        
        Message msg2 = q.peak();
        assertSame(msg, msg2);
        assertEquals(1, q.count());
        
        Message msg3 = new SampleMessage();
        q.enqueue(msg3);
        assertEquals(2, q.count());
        Message msg4 = q.peak();
        assertSame(msg3, msg4);
        
        q.delete(msg4.id());
        assertEquals(1, q.count());
        Message msg5 = q.peak();
        assertSame(msg2, msg5);
        
        q.delete(msg2.id());
        assertEquals(0, q.count());
        Message msg6 = q.peak();
        assertNull(msg6);
    }
}

class SampleMessage
    implements Message
{
    private long id;
    
    public long id()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
    
}
