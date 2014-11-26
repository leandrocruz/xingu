package xingu.cloud.spawner.impl.google.model;

import java.util.List;

public class ServiceAccount
{
	private String email;
	
	private List<String> scopes;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public List<String> getScopes()
	{
		return scopes;
	}

	public void setScopes(List<String> scopes)
	{
		this.scopes = scopes;
	}
}
