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
public class LookUpWithStacks extends  Thread {
    private final String globalDomain ;
    private final ReentrantLock lock = new ReentrantLock();
    private final Pattern  patternInclude = Pattern.compile("include:(?:[_\\+\\*\\-\\+a-z0-9](?:[a-z_\\-\\+\\*0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]");
    private final  Pattern  patternRedirect = Pattern.compile("redirect=(?:[_\\+\\*\\-\\+a-z0-9](?:[a-z_\\-\\+\\*0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]");

    public LookUpWithStacks(String Domain) {
        this.globalDomain = Domain;
    }

    public void run() {


          LookupDomain(this.globalDomain);

    }

    private   void LookupDomain(String initialDomain) {

        Deque<String> domainStack = new LinkedList<>();
        Set<String> processedDomains = new HashSet<>();

        domainStack.push(initialDomain);

        while (!domainStack.isEmpty()) {
            String currentDomain = domainStack.pop();

            if (!processedDomains.contains(currentDomain)) {
                processSPFRecord(currentDomain);
                processedDomains.add(currentDomain);

                // Extract include and redirect domains and add to stack
                List<String> includeDomains = extractIncludeDomains(currentDomain);
                List<String> redirectDomains = extractRedirectDomains(currentDomain);

                domainStack.addAll(includeDomains);
                domainStack.addAll(redirectDomains);
            }
        }

    }

    private    void processSPFRecord(String domain) {


        try{
            String record ="";
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory");
            DirContext dirContext = new InitialDirContext(env);

            Attributes attrs = dirContext.getAttributes(domain, new String[]{"TXT"});

            Attribute txt = attrs.get("TXT");
            if (txt != null) {
                NamingEnumeration<?> e = txt.getAll();
                while (e.hasMore()) {
                    record = e.next().toString();
                    if (!record.contains("v=DMARC1")){
                        writeInFile("nodmarckdoms.txt", domain);
                    }
                    if (record.contains("v=spf1")) {
                        filterRecord(record, domain);
                    }


                }
            }
            else{

                    System.out.println("No Txt record found for ---------------------" + domain);


            }
        }
            catch(NamingException ex){

                    System.out.println("DNS not found for ---------------------" + domain);
                    writeInFile("domain_disabled_temp.txt", domain);


            }
    }

    private  List<String> extractIncludeDomains(String spfRecord) {
        return findDomains(spfRecord,patternInclude);
    }

    private  List<String> extractRedirectDomains(String spfRecord) {
      return findDomains(spfRecord,patternRedirect);
    }

    private  List<String> findDomains(String input, Pattern pattern) {
        List<String> domains = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            domains.add(matcher.group());
        }

        return domains;
    }
    private  Map<String, String> getConditions(boolean exists) {
        Map<String, String> conditions = new HashMap<>();

        String prefix = exists ? "exist" : "only";
        conditions.put("+all", prefix + "_Plus_all_temp.txt");
        conditions.put("-all", prefix + "_minus_all_temp.txt");
        conditions.put("~all", prefix + "_wavy_all_temp.txt");
        conditions.put("?all", prefix + "_question_all_temp.txt");

        return conditions;
    }


    //fiter the record sent by the lookup
    public  void filterRecord(String record, String domain) {
        Map<String, String> conditions = getConditions(record.indexOf("exists") > 0);

        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            String condition = entry.getKey();
            String fileName = entry.getValue();

            if (record.contains(condition)) {
                writeInFile(fileName, domain);
            }
        }
    }
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
