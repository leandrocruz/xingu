package br.com.ibnetwork.xingu.l10n;

import java.util.Locale;
import java.util.ResourceBundle;

public interface LocalizationManager
{
	/*
	 * Default locale 
	 */
	ResourceBundle getResourceBundle();
	
	ResourceBundle getResourceBundle(Locale locale);

}
