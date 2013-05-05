package xingu.node.commons.crypto.impl;

import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.crypto.SymmetricKey;
import xingu.node.commons.crypto.SessionKeyHolder;

public class SessionKeyHolderImpl
	implements SessionKeyHolder
{
    /*
     * see http://java.sun.com/j2se/1.4.2/docs/api/java/util/Collections.html#synchronizedMap(java.util.Map)
     */
    private Map<Object, SymmetricKey> map = new HashMap<Object, SymmetricKey>();
    
    @Override
    public synchronized SymmetricKey symmetricKey(long id)
    {
        return map.get(id);
    }

    @Override
    public synchronized void symmetricKey(long id, SymmetricKey sessionKey)
    {
        map.put(id, sessionKey);
    }

    @Override
    public synchronized void release(long id)
    {
        map.remove(id);
    }
}