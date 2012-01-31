package br.com.ibnetwork.xingu.validator.validators;

public class MessageValidator
	extends ValidatorSupport
{
	public MessageValidator(String messageId, String message)
	{
		_messageId = messageId;
		_message = message;
	}
}