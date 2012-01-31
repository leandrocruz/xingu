package br.com.ibnetwork.xingu.validator.validators.br;

import br.com.ibnetwork.xingu.utils.br.Placa;
import br.com.ibnetwork.xingu.validator.ann.br.ValidatePlaca;
import br.com.ibnetwork.xingu.validator.validators.ValidatorSupport;

public class PlacaValidator
    extends ValidatorSupport
{
	public PlacaValidator(ValidatePlaca ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
	}

	@Override
    public boolean apply(Object bean, String placa)
    {
	    placa = Placa.clearMask(placa);
		if (placa.length() != 7)
		{
			return false;
		}
        
        return Placa.validaPlaca(placa);
    }
}