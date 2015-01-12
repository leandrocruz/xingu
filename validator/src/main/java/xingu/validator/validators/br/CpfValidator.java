package xingu.validator.validators.br;

import xingu.utils.br.Cpf;
import xingu.validator.ann.br.ValidateCpf;
import xingu.validator.validators.ValidatorSupport;

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