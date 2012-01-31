package xingu.store.impl.prevayler.snapshot;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.prevayler.Prevayler;
import org.prevayler.implementation.PrevaylerDirectory;

import xingu.store.SnapshotListener;

public class CleaningSnapshooter
    extends SnapshooterSupport
{
    private File oldDirectory;

    protected File prevalenceDirectory;
    
    public CleaningSnapshooter(SnapshotListener listener, Prevayler prevayler, String prevalenceDirectoryName, Configuration conf)
    {
        super(listener, prevayler);
        this.prevalenceDirectory = new File(prevalenceDirectoryName);
        String oldDirectoryPath = conf.getAttribute("oldDirectory", "old");
        oldDirectory = new File(oldDirectoryPath);
        if(!oldDirectory.isAbsolute())
        {
            oldDirectory = new File(prevalenceDirectory, "old");
        }
        logger.info("Old prevayler files will be moved to '{}'", oldDirectory);
    }

    @Override
    protected void deleteOldFiles()
    {
        File[] files = prevalenceDirectory.listFiles();
        if (files == null)
        {
            logger.info("No files found to move");
            return;
        }
        Set<File> necessaryFiles = necessaryFiles(files);
        for(File file : files)
        {
            if(file.isFile() && !necessaryFiles.contains(file))
            {
                move(file);
            }
        }
    }

    private void move(File file)
    {
        try
        {
            logger.info("Moving file '{}'", file);
            FileUtils.moveFileToDirectory(file, oldDirectory, true);
        }
        catch (IOException e)
        {
            logger.error("Error moving file '"+file+"'", e);
        }
    }

    private Set<File> necessaryFiles(File[] files)
    {
        File journal = latestFile(files, Snapshot.INSTANCE);
        File snapshot = latestFile(files, Journal.INSTANCE);
        Set<File> result = new HashSet<File>();
        if(journal != null) result.add(journal);
        if(snapshot != null) result.add(snapshot);
        return result;
    }

    private File latestFile(File[] files, FileType type)
    {
        File latestFile = null;
        long latestVersion = -1;
        for (int i = 0; i < files.length; i++) 
        {
            File file = files[i];
            long version = type.versionOf(file);
            if (version > latestVersion) 
            {
                latestVersion = version;
                latestFile = file;
            }
        }
        return latestFile;
    }
    
    private static interface FileType
    {
        long versionOf(File file);
    }
    
    private static class Snapshot
        implements FileType
    {
        public static final FileType INSTANCE = new Snapshot();
        
        @Override
        public long versionOf(File file)
        {
            return PrevaylerDirectory.snapshotVersion(file);
        }
    }
    
    private static class Journal
        implements FileType
    {
        public static final FileType INSTANCE = new Journal();
        
        @Override
        public long versionOf(File file)
        {
            return PrevaylerDirectory.journalVersion(file);
        }
    }
}
