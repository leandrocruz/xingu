package xingu.url;

import java.io.Serializable;

public interface DomainName
    extends Serializable
{
    String fullName();

    String registeredName();

    boolean isTopLevelDomain();

    boolean isLocalHost();
}