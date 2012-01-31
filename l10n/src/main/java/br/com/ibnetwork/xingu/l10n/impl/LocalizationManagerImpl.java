package br.com.ibnetwork.xingu.l10n.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.xingu.l10n.LocalizationManager;


public class LocalizationManagerImpl
    implements LocalizationManager, Configurable
{
	private Locale defaultLocale;
	
	private Map<Locale, ResourceBundle> cache = new HashMap<Locale, ResourceBundle>();
	
	private String resourceFile;
	
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		String lang = conf.getChild("locale").getAttribute("language","pt");
		String country = conf.getChild("locale").getAttribute("country","BR");
		defaultLocale = new Locale(lang, country);
		resourceFile = conf.getChild("resources").getAttribute("file","XinguResources");
	}

	public ResourceBundle getResourceBundle()
	{
		return getResourceBundle(defaultLocale);
	}

	public ResourceBundle getResourceBundle(Locale locale)
    {
		ResourceBundle bundle = cache.get(locale);
	    if(bundle == null)
	    {
	    	bundle = ResourceBundle.getBundle(resourceFile, locale);
	    	cache.put(locale, bundle);
	    }
		return bundle;
    }
}
