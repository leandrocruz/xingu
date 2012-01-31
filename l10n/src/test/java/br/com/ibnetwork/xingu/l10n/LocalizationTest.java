package br.com.ibnetwork.xingu.l10n;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class LocalizationTest
    extends XinguTestCase
{
	@Inject
	private LocalizationManager l10n;
	
	@Test
	public void testLocale()
		throws Exception
	{
		assertEquals("peh", l10n.getResourceBundle().getString("foot"));
		assertEquals("pehs", l10n.getResourceBundle().getString("feet"));
		assertEquals("foot", l10n.getResourceBundle(Locale.US).getString("foot"));
		assertEquals("", l10n.getResourceBundle(Locale.US).getString("feet"));
	}
}
