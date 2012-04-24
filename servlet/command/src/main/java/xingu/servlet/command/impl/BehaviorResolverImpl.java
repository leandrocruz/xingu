package xingu.servlet.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.servlet.command.BehaviorResolver;
import xingu.servlet.command.Command;
import xingu.servlet.command.CommandBehavior;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.Injector;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public class BehaviorResolverImpl
    implements BehaviorResolver
{
    @Inject
    private Injector injector;
    
    @Inject
    private Factory factory;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public CommandBehavior behaviorFor(Command command)
    {
        CommandBehavior result = null;
        if(command instanceof CommandBehavior)
        {
            CommandBehavior behavior = (CommandBehavior) command;
            result = behavior;
            inject(result);
        }
        else
        {
            result = tryToApplyConvention(command);
        }
        return result;
    }

    private CommandBehavior tryToApplyConvention(Command command)
    {
        String className = command.getClass().getName();
        className = className.replace("client", "server");
        className += "Behavior";
        Class<?> clazz = null;
        
        try
        {
            clazz = ObjectUtils.loadClass(className);
        }
        catch(Throwable t) 
        {}
        
        if(clazz != null) 
        {
            return (CommandBehavior) factory.create(clazz);
        }
        return null;
    }

    private void inject(CommandBehavior result)
    {
        try
        {
            injector.injectDependencies(result);
        }
        catch (Exception e)
        {
            logger.warn("Error injecting dependencies on " + result, e);
        }
    }
}
