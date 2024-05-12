package org.domain.available;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class main {

    public static  void main(String[] args) throws IOException {

       // String filePath = "C:\\Users\\dev_team\\Desktop\\results\\temp_domains.txt";


        // Load all accounts in a stack
        String filePath = "C:\\Users\\dev_team\\Desktop\\folder\\accounts.txt";
        BufferedReader reader = Files.newBufferedReader(Path.of(filePath));
        Deque<String> accounts = new LinkedList<>(reader.lines().toList());


        // Loop over lines in the file
        String line;
        int i =0;
        Set<String> domains = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            domains.add(line);
            i++;
            if(i%8000==0){
                Check_Availability_Namecheap checker = new Check_Availability_Namecheap(accounts.getFirst().split(";")[0] ,accounts.getFirst().split(";")[3], domains);
                checker.start();
                accounts.removeFirst();
                domains.clear();
                i=0;
            }

        }



    }
}
