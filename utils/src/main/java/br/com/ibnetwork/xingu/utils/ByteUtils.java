package br.com.ibnetwork.xingu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ByteUtils
{
    public static byte[] compress(String name, byte[] data) 
        throws IOException
    {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);
        zipOut.putNextEntry(new ZipEntry(name));
        zipOut.write(data);
        zipOut.closeEntry();
        zipOut.close();
        
        byte[] result = baos.toByteArray();

        baos.close();
        return result;
    }


}
