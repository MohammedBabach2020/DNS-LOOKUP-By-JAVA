package org.example;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LookUpRecrusive extends  Thread {


      static String globalDomain ;

    public LookUpRecrusive(String domain) {
        globalDomain = domain;
    }

    //function to open and write on a txt file
    static ReentrantLock lock = new ReentrantLock();
    private static void writeInFile(String fileName, String domain) {
        try {

            lock.lock();

            String filePath = "C:\\Users\\dev_team\\Desktop\\results\\"+fileName;
            boolean fileExists = Files.exists(Path.of(filePath));

            if(fileExists){
                Files.write(Path.of(filePath), (domain + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
            else{
                Files.createFile(Path.of(filePath));
                Files.write(Path.of(filePath), (domain + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            print("File is beeing used by another process");
            lock.unlock();
        }
        finally {
            lock.unlock();
        }
    }

    //Check the condition to know wich file to use
    private static Map<String, String> getConditions(boolean exists) {
        Map<String, String> conditions = new HashMap<>();

        String prefix = exists ? "exist" : "only";
        conditions.put("+all", prefix + "_Plus_all.txt");
        conditions.put("-all", prefix + "_minus_all.txt");
        conditions.put("~all", prefix + "_wavy_all.txt");
        conditions.put("?all", prefix + "_question_all.txt");

        return conditions;
    }


    //fiter the record sent by the lookup
    public static void filterRecord(String record, String domain) {
        Map<String, String> conditions = getConditions(record.indexOf("exists") <= 0);

        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            String condition = entry.getKey();
            String fileName = entry.getValue();

            if (record.contains(condition)) {
                writeInFile(fileName, domain);
            }
        }
    }

    private static List<String> findDomains(String input, Pattern pattern) {
        List<String> domains = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            domains.add(matcher.group());
        }

        return domains;
    }

    private static void print(Object s){
        System.out.println(s);
    }


    public static void LookupDomain(String Domain) {

        Pattern patternInclude = Pattern.compile("include:(?:[_\\+\\*\\-\\+a-z0-9](?:[a-z_\\-\\+\\*0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]");
        Pattern patternRedirect = Pattern.compile("redirect=(?:[_\\+\\*\\-\\+a-z0-9](?:[a-z_\\-\\+\\*0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]");
        String record = "";
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory");
            DirContext dirContext = new InitialDirContext(env);

            Attributes attrs = dirContext.getAttributes(Domain, new String[]{"TXT"});

            Attribute txt = attrs.get("TXT");
            if (txt != null) {
                NamingEnumeration e = txt.getAll();
                while (e.hasMore()) {
                    record = e.next().toString();

                    if (record.contains(("v=spf1"))) {
                        filterRecord(record, Domain);
                        if (record.contains("include:")) {
                            List<String> domains_Include = findDomains(record, patternInclude);
                            for (String dom : domains_Include) {
                                if (!dom.replace("include:", "").equals(Domain)) {
                                    LookupDomain(dom.replace("include:", ""));
                                }

                            }
                        }
                        if (record.contains("redirect=")) {
                            List<String> domains_redirect = findDomains(record, patternRedirect);
                            for (String dom : domains_redirect) {
                                if (!dom.replace("redirect=", "").equals(Domain)) {
                                    LookupDomain(dom.replace("redirect=", ""));
                                }

                            }
                        }
                    }

                }
            } else {

                System.out.println("No TXT record for -------- " + Domain);
            }
        } catch (NamingException ex) {
            print("DNS not found for ---------------------" + globalDomain);


        } catch (StackOverflowError | Exception e) {
            print("Problem found for ----------------------------------------" + globalDomain);
            writeInFile("domains_problems.txt", globalDomain);
            try {
                Thread.currentThread().join();

            } catch (InterruptedException ex) {
                print("Thread Interuppted");
                Thread.currentThread().interrupt();
            }

        }


        try {
            Thread.sleep(15);
        } catch (InterruptedException ex) {

        }


    }
    public void run() {


            LookupDomain(globalDomain);

    }

    /*

    public static void main(String[] args) {

        String dom = "redirect=facebook.com";
        String Domain = "facebook.com";
        if(!dom.replace("redirect=","").equals(Domain)){
            LookupDomain(dom.replace("redirect=", ""));
        }
        else{
            print("heml");
        }
      //  LookupDomain("spfref.jackhenry");
    }

     */


}
