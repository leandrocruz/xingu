package xingu.node.commons.crypto;

import br.com.ibnetwork.xingu.crypto.SymmetricKey;

public interface SessionKeyHolder
{
    SymmetricKey symmetricKey(long id);

    void symmetricKey(long id, SymmetricKey sessionKey);

    void release(long id);
}
