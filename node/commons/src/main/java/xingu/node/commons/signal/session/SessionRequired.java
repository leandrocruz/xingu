package xingu.node.commons.signal.session;

public class SessionRequired
    extends HandShakeSignal
{
    private String algorithm;
    
    private int size;

    private String publicKeyHash;
    
    private String clientAddress;

    public SessionRequired()
    {
        this("AES", 128, null, null);
    }
    
    public SessionRequired(String algorithm, int size, String publicKeyHash, String clientAddress)
    {
        this.algorithm = algorithm;
        this.size = size;
        this.publicKeyHash = publicKeyHash;
        this.clientAddress = clientAddress;
    }

    public String symmetricKeyAlgorithm()
    {
        return algorithm;
    }
    
    public int keySize()
    {
        return size;
    }

    public String publicKeyHash()
    {
        return publicKeyHash;
    }

	public String clientAddress()
	{
		return clientAddress;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof SessionRequired))
		{
			return false;
		}
		SessionRequired other = (SessionRequired) obj;
		return size == other.size
				&& algorithm == null ? other.algorithm == null : algorithm.equals(other.algorithm)
				&& publicKeyHash == null ? other.publicKeyHash == null : publicKeyHash.equals(other.publicKeyHash)
				&& clientAddress == null ? other.clientAddress == null : clientAddress.equals(other.clientAddress);
	}
}