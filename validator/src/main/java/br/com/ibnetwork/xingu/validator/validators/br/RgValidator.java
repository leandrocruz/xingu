package br.com.ibnetwork.xingu.validator.validators.br;

import org.apache.commons.lang.StringUtils;
import br.com.ibnetwork.xingu.utils.br.Rg;
import br.com.ibnetwork.xingu.validator.ann.br.ValidateRg;
import br.com.ibnetwork.xingu.validator.validators.ValidatorSupport;

public class RgValidator
    extends ValidatorSupport
{
    public RgValidator(ValidateRg ann)
    {
        _message = ann.message();
        _messageId = ann.messageId();
    }
    
    @Override
    public boolean apply(Object bean, String rg)
    {
        rg = Rg.clearMask(rg);
        if (rg.equals("0")) { return false; }
        rg = StringUtils.leftPad(rg, 9, '0');
        if (rg.length() < 6) { return false; }
        boolean result = Rg.calcDigVerif(rg);
        
        return result;
    }
}
