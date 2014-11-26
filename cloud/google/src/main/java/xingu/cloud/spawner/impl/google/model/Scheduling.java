package xingu.cloud.spawner.impl.google.model;

public class Scheduling
{
	private boolean automaticRestart;
	
	private String onHostMaintenance;

	public boolean isAutomaticRestart()
	{
		return automaticRestart;
	}

	public void setAutomaticRestart(boolean automaticRestart)
	{
		this.automaticRestart = automaticRestart;
	}

	public String getOnHostMaintenance()
	{
		return onHostMaintenance;
	}

	public void setOnHostMaintenance(String onHostMaintenance)
	{
		this.onHostMaintenance = onHostMaintenance;
	}
}
