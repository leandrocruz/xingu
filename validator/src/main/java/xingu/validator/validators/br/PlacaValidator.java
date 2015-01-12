package xingu.validator.validators.br;

import xingu.utils.br.Placa;
import xingu.validator.ann.br.ValidatePlaca;
import xingu.validator.validators.ValidatorSupport;

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