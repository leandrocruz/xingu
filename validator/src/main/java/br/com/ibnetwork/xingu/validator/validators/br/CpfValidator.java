package br.com.ibnetwork.xingu.validator.validators.br;

import br.com.ibnetwork.xingu.utils.br.Cpf;
import br.com.ibnetwork.xingu.validator.ann.br.ValidateCpf;
import br.com.ibnetwork.xingu.validator.validators.ValidatorSupport;

public class CpfValidator
    extends ValidatorSupport
{
	public CpfValidator(ValidateCpf ann)
	{
		_message   = ann.message();
		_messageId = ann.messageId();
	}
	
	@Override
    public boolean apply(Object bean, String cpf)
    {
	    return Cpf.isValid(cpf);
    }
}