package org.example;


import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Hashtable;
import java.util.concurrent.locks.ReentrantLock;

public class LookUpDmarck extends  Thread {
    private final String globalDomain ;

    public LookUpDmarck(String Domain) {
        this.globalDomain = Domain;
    }
    public void run() {


        LookupDomain(this.globalDomain);

    }


     private  void  LookupDomain(String domain) {
        try{
            String record ="";
            Resolver resolver = new SimpleResolver();
            Lookup lookup = new Lookup("_dmarc."+domain, Type.TXT);
            lookup.setResolver(resolver);
            Record[] records = lookup.run();


            if (records == null ) {
              writeInFile("nodmarckdoms2.txt", domain);
            }
            else{
                if (records.length == 0) {

                    writeInFile("nodmarckdoms.txt", domain);
                }

            }

        }

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }






    private final ReentrantLock lock = new ReentrantLock();

    private  void writeInFile(String fileName, String domain) {
        try {

            lock.lock();

            String filePath = "C:\\Users\\babach\\Desktop\\salah\\"+fileName;
            boolean fileExists = Files.exists(Path.of(filePath));

            if(fileExists){
                Files.write(Path.of(filePath), (domain + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
            else{
                Files.createFile(Path.of(filePath));
                Files.write(Path.of(filePath), (domain + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            System.out.println("File is being used by another process");
            lock.unlock();
        }
        finally {
            lock.unlock();
        }
    }


}
