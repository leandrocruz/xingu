package br.com.ibnetwork.xingu.crypto.impl.rsa.app;

import java.util.Scanner;

public class RSA
{
    public static final void main(String[] args)
        throws Exception
    {
        Command cmd = null;
        String commandName = null;
        Scanner in = new Scanner(System.in);
    
        if(args.length == 0)
        {
            System.out.println("What do you want do do ? ");
            commandName = in.next();
        }
        else
        {
            commandName = args[0];
        }

        if("generateKeyPair".equals(commandName) ||
                "gkp".equals(commandName))
        {
            System.out.println("Which size (1024/2048/4096) ? ");
            int length = in.nextInt();
            if(length <= 0)
            {
                length = 1024;
            }
            cmd = new GenerateKeypair(length);
        }
        else if("enc".equals(commandName))
        {
            System.out.println("Where is the key ? ");
            String key = in.next();
            System.out.println("Where is the file ? ");
            String file = in.next();
            cmd = new EncryptFile(key, file);
        }
        
        
        if(cmd == null)
        {
            System.out.println("Command not found for: "+commandName);
        }
        else
        {
            cmd.exec();
        }
    }
}
