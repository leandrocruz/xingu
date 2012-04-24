package xingu.servlet.container;

import java.util.Collection;

public interface ServletContainer
{
    ApplicationContext context(String name);

    Collection<ApplicationContext> contexts();
}
