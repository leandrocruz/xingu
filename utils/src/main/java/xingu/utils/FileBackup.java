package xingu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import org.apache.commons.io.IOUtils;

import xingu.lang.NotImplementedYet;

public class FileBackup
{
    private File file;
    
    public FileBackup(String filename)
    {
        file = new File(filename);
    }

    public File backup() 
        throws Exception
    {
        if(!file.exists())
        {
            return null;
        }
        if(file.isDirectory())
        {
            throw new NotImplementedYet();
        }
        String name = nextBackupName();
        File backup = new File(file.getParent(), name);
        IOUtils.copy(new FileInputStream(file), new FileOutputStream(backup));
        return backup;
    }

    private String nextBackupName()
    {
        String orig = file.getName();
        final String s = orig+".bck";
        File parent = file.getParentFile();
        String[] candidates = parent.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.startsWith(s);
            }
        });
        if(candidates == null || candidates.length == 0)
        {
            return s+"0";
        }
        int greater = 0;
        for (String candidate : candidates)
        {
            int idx = candidate.lastIndexOf(".");
            String number = candidate.substring(idx+4);
            int numberAsInt = Integer.parseInt(number);
            if(numberAsInt > greater)
            {
                greater = numberAsInt;
            }
        }
        return s+(greater+1);
    }

}
