package xingu.crypto.impl;

import java.security.Key;

import xingu.crypto.PubKey;

public class NullPublicKey
	implements PubKey
{

	@Override
	public String getAlgorithm()
	{
		return null;
	}

	@Override
	public String getFormat()
	{
		return null;
	}

	@Override
	public byte[] getEncoded()
	{
		return null;
	}

	@Override
	public Key wrappedKey()
	{
		return null;
	}

	@Override
	public long keyId()
	{
		return 0;
	}

	@Override
	public boolean isEncryptionKey()
	{
		return false;
	}

	@Override
	public int bitStrength()
	{
		return 0;
	}

	@Override
	public String hash()
	{
		return "NullPublicKey";
	}

}
