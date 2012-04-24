package xingu.servlet.command;

import java.io.Serializable;

public interface CommandReply
    extends Serializable
{
    String OK = "OK";
    
    String ERROR = "ERROR";
    
    String result();
}
