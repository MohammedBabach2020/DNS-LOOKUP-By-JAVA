package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main  extends Thread  {


     public static  void  main(String[] args) {

        String longquery ="";
         try {


            String filePath = "C:\\Users\\babach\\Desktop\\salah\\d.txt";
            BufferedReader reader = Files.newBufferedReader(Path.of(filePath));

            // Loop over lines in the file
            String line;
            int i =0;
            while ((line = reader.readLine()) != null) {
                i++;
                LocalDateTime DateTimeObject = LocalDateTime.now();
                DateTimeFormatter DatePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = DateTimeObject.format(DatePattern);
                 System.out.println(formattedDate + ": " +  i + "--------------------------" + line);
                //LookUpWithStacks runner = new LookUpWithStacks(line);
                LookUpDmarck runner = new LookUpDmarck(line);
                 runner.start();
                Files.write(Path.of("C:\\Users\\babach\\Desktop\\salah\\logs.txt"), (formattedDate +"      " + i +"      " + line + System.lineSeparator()).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            }

            // Close the BufferedReader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception ex){

            ex.printStackTrace();
        }

    }

}

