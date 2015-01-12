package xingu.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import xingu.utils.FSUtils;
import junit.framework.TestCase;

public class FSUtilsTest
    extends TestCase
{
    private String root;
    
    @Override
    protected void setUp() 
        throws Exception {
        root = FSUtils.load(".");   
    }

    public void testLoadRoot()
        throws Exception
    {
        String fileName = FSUtils.load("/");
        assertEquals("/",fileName);
    }
    
    public void testLoadResourceFromClasspath()
        throws Exception
    {  
        String fileName = FSUtils.load("file.txt");
        assertTrue(fileName.endsWith("file.txt"));
        File file = new File(fileName);
        assertTrue(file.exists());
        //assertEquals(FilenameUtils.concat(root, "file.txt"),fileName);
    }
    
    public void testLoadResourceFromJar()
    {
        String fileName = FSUtils.loadFromClasspath("org/junit/Test.class");
        assertNotNull(fileName);
        assertTrue(fileName.endsWith("Test.class"));
    }

    public void testLoadAbsoluteResource()
        throws Exception
    {
        String fullPath = FilenameUtils.concat(root, "file.txt");
        String fileName = FSUtils.load(fullPath);
        assertEquals(fullPath,fileName);
    }

    public void testLoadResourceNotFoundRelative()
        throws Exception
    {
        String fileName = FSUtils.load("noFile.abcd");
        assertNull(fileName);
    }
    
    public void testLoadResourceNotFoundAbsolute()
        throws Exception
    {
        String fileName = FSUtils.load("/noFile.abcd");
        assertEquals("/noFile.abcd",fileName);
    }
    
    public void testLoadFile()
        throws Exception
    {
        File file = FSUtils.loadAsFile("fileUtils/file.txt");
        assertTrue(file.exists());
        assertEquals("file.txt",file.getName());
        
        file = FSUtils.loadAsFile("/fileUtils/file.txt");
        assertFalse(file.exists());
    }

    public void testCloneEmptyFile()
        throws Exception
    {
        File file = FSUtils.loadAsFile("fileUtils/empty.txt");
        assertTrue(file.exists());
        File clone = FSUtils.clone(file);
        assertTrue(clone.exists());
        assertTrue(FSUtils.compare(file,clone));
        org.apache.commons.io.FileUtils.forceDelete(clone);
    }

    public void testCloneFile()
        throws Exception
    {
        File file = FSUtils.loadAsFile("fileUtils/file.txt");
        assertTrue(file.exists());
        File clone = FSUtils.clone(file);
        assertTrue(clone.exists());
        assertTrue(FSUtils.compare(file,clone));
        org.apache.commons.io.FileUtils.forceDelete(clone);
    }

    public void testCloneDir()
        throws Exception
    {
        File file = FSUtils.loadAsFile("fileUtils/folder");
        assertTrue(file.exists());
        File clone = FSUtils.clone(file);
        assertTrue(clone.exists());
        assertTrue(FSUtils.compare(file,clone));
        org.apache.commons.io.FileUtils.forceDelete(clone);
    }


}
