package br.com.ibnetwork.xingu.validator.validators.br;

import br.com.ibnetwork.xingu.utils.br.Renavam;
import br.com.ibnetwork.xingu.validator.ann.br.ValidateRenavam;
import br.com.ibnetwork.xingu.validator.validators.ValidatorSupport;


public class RenavamValidator
    extends ValidatorSupport
{
	public RenavamValidator(ValidateRenavam ann)
	{
		_message = ann.message();
		_messageId = ann.messageId();
	}

	@Override
    public boolean apply(Object bean, String renavam)
    {
        renavam = Renavam.clearMask(renavam);
        
        if (renavam.length() != 9)
        {
            return false;
        } 

        String numDig = renavam.substring(0, 8);
        String digit = Renavam.calcDigVerif(numDig);
        boolean result = digit.equals(renavam.substring(8, 9)); 
        result = true;
		return result;
    }
}