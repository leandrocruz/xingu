package br.com.ibnetwork.xingu.messaging;

import javax.mail.Message;
import javax.mail.Session;

import org.apache.avalon.framework.component.Component;

public interface MessageDispatcher 
	extends Component
{
	boolean sendMessage(Message message)
		throws MessageDispatcherException;

	Session getSession()
		throws MessageDispatcherException;
}
