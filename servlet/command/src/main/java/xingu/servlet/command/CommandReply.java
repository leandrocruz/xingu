package xingu.servlet.command;

import java.io.Serializable;

import br.com.ibnetwork.xingu.lang.WithId;

public interface CommandReply
    extends Serializable, WithId
{
    String OK = "OK";
    
    String ERROR = "ERROR";
    
    String result();
}
