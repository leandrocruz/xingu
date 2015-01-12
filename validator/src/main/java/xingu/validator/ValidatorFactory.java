package xingu.validator;

import java.lang.annotation.Annotation;

import xingu.factory.Factory;
import xingu.validator.ann.ValidateBadChar;
import xingu.validator.ann.ValidateDate;
import xingu.validator.ann.ValidateEmail;
import xingu.validator.ann.ValidateJava;
import xingu.validator.ann.ValidateMinValue;
import xingu.validator.ann.ValidateRequired;
import xingu.validator.ann.br.ValidateCep;
import xingu.validator.ann.br.ValidateCpf;
import xingu.validator.ann.br.ValidatePlaca;
import xingu.validator.ann.br.ValidateRenavam;
import xingu.validator.ann.br.ValidateRg;
import xingu.validator.validators.BadCharValidator;
import xingu.validator.validators.DateValidator;
import xingu.validator.validators.EmailValidator;
import xingu.validator.validators.MinValueValidator;
import xingu.validator.validators.RequiredValidator;
import xingu.validator.validators.Validator;
import xingu.validator.validators.br.CepValidator;
import xingu.validator.validators.br.CpfValidator;
import xingu.validator.validators.br.PlacaValidator;
import xingu.validator.validators.br.RenavamValidator;
import xingu.validator.validators.br.RgValidator;


public class ValidatorFactory
{
	public static Validator createFromAnnotation(Annotation ann, Factory factory)
    {
		Class<? extends Validator> clazz = null;
		if(ann instanceof ValidateEmail)
		{
			clazz = EmailValidator.class;
		}
		else if(ann instanceof ValidateRequired)
		{
			clazz = RequiredValidator.class;
		}
		else if(ann instanceof ValidateCep)
		{
			clazz = CepValidator.class;
		}
		else if(ann instanceof ValidateCpf)
		{
			clazz = CpfValidator.class;
        }
        else if(ann instanceof ValidateRg)
        {
            clazz = RgValidator.class;
        }
        else if(ann instanceof ValidatePlaca)
        {
            clazz = PlacaValidator.class;
        }
        else if(ann instanceof ValidateRenavam)
        {
            clazz = RenavamValidator.class;
        }                
		else if(ann instanceof ValidateJava)
		{
			ValidateJava tmp = (ValidateJava) ann;
			clazz = tmp.validatorClass();
		}
		else if(ann instanceof ValidateMinValue)
		{
			clazz = MinValueValidator.class;
		}
		else if(ann instanceof ValidateDate)
		{
			clazz = DateValidator.class;
		}
		else if(ann instanceof ValidateBadChar)
		{
			clazz = BadCharValidator.class;
		}
		if(clazz != null)
		{
			Validator validator = factory.create(clazz, ann);
			return validator;
		}
		return null;
    }
}
