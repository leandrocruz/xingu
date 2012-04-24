package xingu.servlet.command;

public interface BehaviorResolver
{
    CommandBehavior behaviorFor(Command command);
}
