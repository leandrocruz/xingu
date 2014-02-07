package xingu.maze.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xingu.maze.Config;


public class ConfigImpl
	implements Config
{
    private Map<String, String> vars;
    
    private List<DomainImpl> domains = new ArrayList<DomainImpl>();
    
    ConfigImpl(Map<String, String> vars)
    {
        this.vars = vars;
    }

    @Override
    public String mainClass()
    {
        return vars.get("main");
    }

	@Override
	public String pidFile()
	{
		return vars.get("pidFile");
	}

    @Override
    public DomainImpl domain(String name)
    {
        for(DomainImpl domain : domains)
        {
            if(domain.name().equals(name))
            {
                return domain;
            }
        }
        return null;
    }

    @Override
    public DomainImpl defaultDomain()
    {
        return domain("default");
    }

    void add(DomainImpl domain)
    {
        domains.add(domain);
    }
}
