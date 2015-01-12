package xingu.node.commons.crypto;

import java.security.PrivateKey;

import xingu.crypto.PubKey;

public interface KeyPair
{
	String id();

	PubKey publicKey()
		throws Exception;

	PrivateKey privateKey()
		throws Exception;

	// void publicKey(PubKey pubKey);
}
