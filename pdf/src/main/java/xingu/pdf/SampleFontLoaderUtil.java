/*
 * ****************************************************************************
 *
 *  Copyright 2009-2012 Adobe Systems Incorporated. All Rights Reserved.
 *  Portions Copyright 2012 Datalogics Incorporated.
 *
 *  NOTICE: Datalogics and Adobe permit you to use, modify, and distribute
 *  this file in accordance with the terms of the license agreement
 *  accompanying it. If you have received this file from a source other
 *  than Adobe or Datalogics, then your use, modification, or distribution of it
 *  requires the prior written permission of Adobe or Datalogics.
 *
 * ***************************************************************************
 */

package xingu.pdf;

import com.adobe.fontengine.font.Font;
import com.adobe.fontengine.font.PDFFontDescription;
import com.adobe.fontengine.fontmanagement.FontLoader;
import com.adobe.fontengine.fontmanagement.postscript.PostscriptFontDescription;
import com.adobe.fontengine.inlineformatting.css20.CSS20GenericFontFamily;
import com.adobe.pdfjt.core.exceptions.PDFFontException;
import com.adobe.pdfjt.core.fontset.PDFFontSet;
import com.adobe.pdfjt.core.fontset.PDFFontSetManager;
import com.adobe.pdfjt.core.fontset.impl.PDFFontSetImpl;
import com.adobe.pdfjt.pdf.document.PDFDocument;
import com.adobe.pdfjt.pdf.graphics.font.impl.StandardFontUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Note that this is a very simple FontSet builder. It exhibits none of the
 * optimizations, error handling, nor robustness that a real production quality
 * FontSet builder should have.
 * 
 * WARNING: this code is for demonstration purposes only and not intended for
 * production usage.
 */
public class SampleFontLoaderUtil {

    /* This contains standard default directories for storing fonts across 
     * Linux, Mac, and Windows systems, which is a good place to start looking 
     * for fonts to load. If you know your system keeps its font files in a 
     * specific directory or you have a subset you wish to load in a given
     * directory, alter this array appropriately. For temporary/experimental 
     * font loading, provide the directory path(s) you wish to load as arguments
     * to the buildSampleFontSet method.
     */
    
    //Contains three font files specifically with JT, loaded as fallback fonts
    private static final String DEFAULT_FONT_DIR = "src/test/resources/fonts";
    
    private static final String[] DEFAULT_FONT_DIRS = {"c:/windows/fonts",
        "/usr/share/fonts", "/Library/Fonts", "/System/Library/Fonts",
        DEFAULT_FONT_DIR};
    
    private static boolean verbose = false;

    /**
     * Blank constructor to prevent this class from being instantiated. This is
     * a utility class and represents a set of operations rather than a proper
     * object.
     */
    private SampleFontLoaderUtil() {}

    /**
     * Loads all the fonts from the default directories (specified by the
     * DEFAULT_FONT_DIRS array in this file's code) as well as the font files 
     * that ship with PDFJT.
     * @return PDFFontSet
     * @throws Exception buildSampleFontSet throws a general exception
     */
    public static PDFFontSet buildSampleFontSet() throws Exception {
        return buildSampleFontSet(new String[0]);
    }
    
    public static void setVerbose(boolean newVerbose)
    {
        verbose = newVerbose;
    }

    /**
     * Loads all the fonts from the default directories (specified by the
     * DEFAULT_FONT_DIRS array in this file's code) as well as the font files 
     * that ship with PDFJT. In addition, loads any font files from directories
     * provided by the user in the argument array.
     * @param userFontDirs Any directories requested by the user
     * @return PDFFontSet
     * @throws Exception General Exception
     */
    public static PDFFontSet buildSampleFontSet(String [] userFontDirs) 
        throws Exception {

        /*
         * Note: That you may also pass a FontResolutionPriority to this factory
         * method to determine what fonts
         */
        PDFFontSet pdfFontSet = PDFFontSetManager.getPDFFontSetInstance();
        pdfFontSet.setIgnoreFontLoadingErrors(true);

        /*
         * NOTE: this method of loading fonts by directory is imprecise and can 
         * lead to different textual results depending on the order that the 
         * fonts are loaded on a particular computer. If you desire 
         * repeatability then load the fonts in a consistent, repeatable manner,
         * such as by explicitly loading the fonts in alphabetical order or
         * something similar.
         */

        try {
            FontLoader loader = new FontLoader();

            List<String> fontDirList = null;
            if (userFontDirs != null && userFontDirs.length > 0) {
                fontDirList = new ArrayList<String>(userFontDirs.length + 
                        DEFAULT_FONT_DIRS.length);
                Collections.addAll(fontDirList, userFontDirs);
            } else {
                fontDirList = new ArrayList<String>(DEFAULT_FONT_DIRS.length);
            }
            Collections.addAll(fontDirList, DEFAULT_FONT_DIRS);

            String[] fontDirectories = 
                    fontDirList.toArray(new String[fontDirList.size()]);

            ArrayList<Exception> exceptionList = new ArrayList<Exception>();
            for(int i = 0; i < fontDirectories.length; i++) {
                Font[] fonts = null;
                File fontDir = new File(fontDirectories[i]);

                if (fontDir.exists()) {
                    fonts = loader.load(fontDir, true, exceptionList);
                    // Add those fonts to the font set
                    for (int j = 0; j < fonts.length; j++) {
                        try {
                            if(fonts[j] != null) {
                                pdfFontSet.addFont(fonts[j]);
                            }
                        } catch (Exception e) {
                            //fonts that cause exceptions to be thrown will
                            //not be embedded
                            exceptionList.add(e);
                        }
                    }
                }
            }

            // Check for any exceptions during font loading
            Iterator<Exception> iter = exceptionList.iterator();
            if( verbose && iter.hasNext()) {
                System.err.println("Font Loading Exceptions:");
                for(Exception e: exceptionList) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.err.println("Font Loading Error: " + e.getMessage());
            } else {
                System.err.println("Font Loading Exception:");
                e.printStackTrace();
            }
        }
        
        //COPY/PASTE from Confluence
        PDFFontSetImpl pdfFontSetImpl = 
                new PDFFontSetImpl((PDFFontSetImpl)pdfFontSet);
        Font[] base14Fonts = StandardFontUtils.getBase14Fonts();
        for(int idxFont = 0; idxFont < base14Fonts.length; idxFont++) {
            if (verbose) {
                System.out.println(base14Fonts[idxFont].toString());
            }
            pdfFontSetImpl.getCSS20FontSet().addFont(base14Fonts[idxFont]);
            PDFFontDescription curDescription = 
                    base14Fonts[idxFont].getPDFFontDescription();
            pdfFontSetImpl.getPSFontSet().addFont(
                new PostscriptFontDescription(curDescription.getBase14Name()), 
                base14Fonts[idxFont]);
        }

        /*
         * Load the fallback fonts into the fontset.
         * 
         * These fallback fonts are used during text layout when a request for a
         * certain font can't be fulfilled. In that case a "fallback" or
         * substitute font is used. That fallback font is taken from the
         * fallback fonts that are provided during the construction of the
         * PDFFontSet.
         */
        loadFallBackFonts(pdfFontSetImpl);

        /*
         * Set the generic font family names in the fontset.
         * 
         * The generic font family names provide a way to find a font under
         * certain types of font requests. These requests can be looking for a
         * font that is 'serif' or 'cursive'. The font names provided here will
         * be substituted for those generic names and then will be searched for
         * just as if the original font request had been for those names.
         */
        setGenericFontFamilyNames(pdfFontSetImpl);

        /*
         * After having created a PDFFontSet from fonts found on the system,
         * added the fallback fonts and set the generic font family names that
         * initial fontset can be used for work with PDFJT.
         * 
         * Loading and setting up a fontset requires that every font that you
         * add to the fontset be loaded and parsed. If you have a large number
         * of fonts then this can take a large amout of time. So, what can be
         * done is that a fontset can be built once (or only when the fonts
         * change) and then serialized using standard Java object serialization.
         * 
         * In some usage scenarios a "master" process may build and control the
         * fontset and then give it out to other processes to use. The problem
         * is that these other processes can then modify the fontset in ways
         * that aren't compatible for others. To avoid this it is possible to
         * clone the fontset (actually a copy constructor but semantically the
         * same) before passing it to others to use. This generates a relatively
         * lightweight object that replicates only the upper layers of the
         * fontset data without requiring reloading of the fonts or that the
         * fonts themselves be copied. There is a requirement that whatever
         * process is going to use that fontset be able to access the actual
         * underlying bits where fonts that are included in it are stored.
         * "system" font set can be cloned. That "clone" can then have temporary
         * fonts added to it for use in a set batch of formatting operations.
         * However, without the fallback fonts set or the generic font families
         * set there will be situations where
         */
        
        if (verbose) {
            System.out.println("*****PDF FONT SET*****");
            System.out.println(pdfFontSetImpl.toString());
        }
        

        return pdfFontSetImpl;
    }

    /**
     * Method to load "Fallback" fonts into given fontset
     *  
     * @param pdfFontSet The documents fontset
     * @throws PDFFontException Font Exception
     */
    private static void loadFallBackFonts(PDFFontSet pdfFontSet)
        throws PDFFontException {
        /*
         * Note: The fonts loaded here aas locale fallbacks will NOT be correct
         * for your application. The only intent of this code is to show you how
         * to do so.
         * 
         * WARNING: this code is for demonstration purposes only and not
         * intended for production usage.
         */
        FontLoader loader = new FontLoader();
        List<Exception> exceptions = new ArrayList<Exception>();

        /*
         * Loading the fonts ONCE and then adding them to the fontset multiple
         * times will avoid multiple Java objects that represent the same font
         * bytes stored on disk.
         * 
         * In a real production quality fontset builder these font objects
         * should probably be the same ones that are loaded as regular fonts
         * into the fontset. This will prevent a second Java object that
         * represents the same font bytes stored on the disk. But then, this is
         * SAMPLE code not intended for production systems but only to show how
         * to use the APIs.
         */
        
        Font[] kozmin    = loader.load(asFile("fonts" + File.separator + "KozMinPro-Regular.otf"), false, exceptions);
        Font[] adobeThai = loader.load(asFile("fonts" + File.separator + "AdobeThai-Regular.otf"), false, exceptions);
        Font[] minion    = loader.load(asFile("fonts" + File.separator + "MinionPro-Regular.otf"), false, exceptions);

        /*
         * After loading we check for errors that may have occurred with the
         * fonts. In real production quality code you would likely want to check
         * after the load of each font.
         */
        Iterator<Exception> exceptionIter = exceptions.iterator();
        if(exceptionIter.hasNext()) {
            // FallbackFonts should not be throwing exceptions, 
            // so if something is caught, there's a serious error somewhere.
            Exception e = exceptionIter.next();
            throw new PDFFontException(e);
        }

        /*
         * We add all the fonts to the ROOT locale, so that no matter what
         * character and what locale combination we run into, we have a good
         * chance of being able to avoid .notdef.
         */
        pdfFontSet.addFallbackFont(PDFDocument.ROOT_LOCALE, minion);
        pdfFontSet.addFallbackFont(PDFDocument.ROOT_LOCALE, kozmin);
        pdfFontSet.addFallbackFont(PDFDocument.ROOT_LOCALE, adobeThai);

        /*
         * For each locale, we do have a preference. This is because there are
         * characters which are common to multiple locales; this is particularly
         * acute for the East Asian locales, but it true everywhere: for
         * example, in the 'ar' locale, we really want to use the latin digits
         * coming from AdobeArabic rather than those of Minion, as they are more
         * likely to mesh well with the Arabic glyphs (and yes, latin digits are
         * used in Arabic, and are in fact the preferred form in North Africa).
         */
        pdfFontSet.addFallbackFont(Locale.JAPANESE, kozmin);
        pdfFontSet.addFallbackFont(new Locale("th"), adobeThai);
    }

    private static File asFile(String name)
	{
    	ClassLoader cl  = Thread.currentThread().getContextClassLoader();
    	URL         url = cl.getResource(name);
    	return new File(url.getFile());
	}

	/**
     * Method to set the generic font family names
     * 
     * @param pdfFontSet PDFFontSet in which to set the family names
     * @throws PDFFontException Font Exception
     */
    private static void setGenericFontFamilyNames(PDFFontSet pdfFontSet)
        throws PDFFontException {
        /*
         * Note: Change the following String arrays so they are correct for your
         * application. The following names are intended to show how to use the
         * API not what font names to use.
         * 
         * In systems designed to support multiple locales then fonts for those
         * locales should be added as generic fonts where there are appropriate
         * fonts available for the generic family name. Not all locales will
         * have fonts that map into these generic family names.
         * 
         * WARNING: this code is for demonstration purposes only and not
         * intended for production usage.
         */
        String[] serif = { "Minion Pro", "Garamond" };
        String[] sans = { "ArialMT", "Gill Sans Std" };
        String[] cursive = { "Zapf-Chancery", "Gill Sans Std" };
        String[] fantasy = { "Minion Pro" };
        String[] monospace = { "CourierNew", "Courier" };

        pdfFontSet.setGenericFontFamilyName(CSS20GenericFontFamily.SANS_SERIF,
                sans);
        pdfFontSet.setGenericFontFamilyName(CSS20GenericFontFamily.SERIF, 
                serif);
        pdfFontSet.setGenericFontFamilyName(CSS20GenericFontFamily.MONOSPACE,
                monospace);
        pdfFontSet.setGenericFontFamilyName(CSS20GenericFontFamily.FANTASY,
                fantasy);
        pdfFontSet.setGenericFontFamilyName(CSS20GenericFontFamily.CURSIVE,
                cursive);
    }
}
