package br.com.ibnetwork.xingu.flow;

public interface Workflow<FACADE extends BusinessFacade> 
{

	public static final String ROLLBACK = "ROLLBACK";
	public static final String END = "END";
	public static final String COMMIT = "COMMIT";
	public static final String BEGIN = "BEGIN";
	
	public String getFirstStep();
	
	public String getNextStep(String currentState, FACADE facade);
}
