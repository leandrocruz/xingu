package xingu.test;

import java.util.Date;

import xingu.validator.ann.ValidateBadChar;
import xingu.validator.ann.ValidateDate;
import xingu.validator.ann.ValidateEmail;
import xingu.validator.ann.ValidateJava;
import xingu.validator.ann.ValidateMinValue;
import xingu.validator.ann.ValidateRequired;
import xingu.validator.ann.ValidateDate.Comparator;
import xingu.validator.ann.ValidateDate.Direction;
import xingu.validator.ann.br.ValidateCep;
import xingu.validator.ann.br.ValidateCpf;
import xingu.validator.ann.br.ValidatePlaca;
import xingu.validator.ann.br.ValidateRenavam;
import xingu.validator.ann.br.ValidateRg;

public class SampleBean
{
	@ValidateRequired
	private String name;
	
	@ValidateRequired(required=false)
	private String middleName;
	
	@ValidateRequired(message="Field required")
	private String lastName;
	
	@ValidateEmail(message="Bad email syntax")
	private String email;
	
	@ValidateJava(validatorClass=UsernameValidator.class)
	private String username;
	
	@ValidateCep
	private String cep;
	
	@ValidateMinValue(minValue=10)
	private double minValue;
	
	@ValidateCpf
	private String cpf;
    
    @ValidateRg
    private String rg;
    
    @ValidatePlaca
    private String placa;
    
    @ValidateRenavam
    private String renavam;
    
    @ValidateBadChar(badChar = "abc")
    private String badChar;
    
    @ValidateDate
    private Date now;
    
    @ValidateDate(date = "1y")
    private Date pastBefore;

    @ValidateDate(date = "1y", comparator = Comparator.AFTER)
    private Date pastAfter;

    @ValidateDate(date = "1y", direction = Direction.FUTURE, comparator = Comparator.AFTER)
    private Date futureAfter;

    @ValidateDate(date = "1y", direction = Direction.FUTURE)
    private Date futureBefore;

	public String getEmail(){return email;}
	public void setEmail(String email){this.email = email;}
	public String getLastName(){return lastName;}
	public void setLastName(String lastName){this.lastName = lastName;}
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	public String getMiddleName(){return middleName;}
	public void setMiddleName(String middleName){this.middleName = middleName;}
	public String getUsername(){return username;}
	public void setUsername(String username){this.username = username;}
	public String getCep(){return cep;}
	public void setCep(String cep){this.cep = cep;}
	public double getMinValue(){return minValue;}
	public void setMinValue(double minValue){this.minValue = minValue;}
	public String getCpf(){return cpf;}
	public void setCpf(String cpf){this.cpf = cpf;}
    public String getRg(){return rg;}
    public void setRg(String rg){this.rg = rg;}    
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getRenavam() { return renavam; }
    public void setRenavam(String renavam) { this.renavam = renavam; }
	public String getBadChar(){return badChar;}
	public void setBadChar(String badChar){this.badChar = badChar;}
	public Date getNow(){return now;}
	public void setNow(Date now){this.now = now;}
	public Date getPastBefore(){return pastBefore;}
	public void setPastBefore(Date pastBefore){this.pastBefore = pastBefore;}
	public Date getPastAfter(){return pastAfter;}
	public void setPastAfter(Date pastAfter){this.pastAfter = pastAfter;}
	public Date getFutureAfter(){return futureAfter;}
	public void setFutureAfter(Date futureAfter){this.futureAfter = futureAfter;}
	public Date getFutureBefore(){return futureBefore;}
	public void setFutureBefore(Date futureBefore){this.futureBefore = futureBefore;}
}
