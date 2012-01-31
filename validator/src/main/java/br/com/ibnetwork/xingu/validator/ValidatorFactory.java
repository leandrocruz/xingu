package br.com.ibnetwork.xingu.validator;

import java.lang.annotation.Annotation;

import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.validator.ann.ValidateBadChar;
import br.com.ibnetwork.xingu.validator.ann.ValidateDate;
import br.com.ibnetwork.xingu.validator.ann.ValidateEmail;
import br.com.ibnetwork.xingu.validator.ann.ValidateJava;
import br.com.ibnetwork.xingu.validator.ann.ValidateMinValue;
import br.com.ibnetwork.xingu.validator.ann.ValidateRequired;
import br.com.ibnetwork.xingu.validator.ann.br.ValidateCep;
import br.com.ibnetwork.xingu.validator.ann.br.ValidateCpf;
import br.com.ibnetwork.xingu.validator.ann.br.ValidatePlaca;
import br.com.ibnetwork.xingu.validator.ann.br.ValidateRenavam;
import br.com.ibnetwork.xingu.validator.ann.br.ValidateRg;
import br.com.ibnetwork.xingu.validator.validators.BadCharValidator;
import br.com.ibnetwork.xingu.validator.validators.DateValidator;
import br.com.ibnetwork.xingu.validator.validators.EmailValidator;
import br.com.ibnetwork.xingu.validator.validators.MinValueValidator;
import br.com.ibnetwork.xingu.validator.validators.RequiredValidator;
import br.com.ibnetwork.xingu.validator.validators.Validator;
import br.com.ibnetwork.xingu.validator.validators.br.CepValidator;
import br.com.ibnetwork.xingu.validator.validators.br.CpfValidator;
import br.com.ibnetwork.xingu.validator.validators.br.PlacaValidator;
import br.com.ibnetwork.xingu.validator.validators.br.RenavamValidator;
import br.com.ibnetwork.xingu.validator.validators.br.RgValidator;


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
