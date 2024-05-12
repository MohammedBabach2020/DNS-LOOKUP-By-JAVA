package org.seedsLooker;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class Main {

    public static void main(String[] args) {

        try {

            String myDirectoryPath = "C:\\Users\\babach\\Desktop\\temp_mounir";
            File dir = new File(myDirectoryPath);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                int i = 0;
                for (File child : directoryListing) {
                    i++;
                    System.out.println("looking inside: " +   child.getName());
                    BufferedReader reader = Files.newBufferedReader(Path.of(child.getPath()) , Charset.forName("ISO-8859-1"));

                    // Loop over lines in the file
                    String line;

                    int j = 0;
                    while ((line = reader.readLine()) != null) {

                        j++;

                            if(line.contains("@ptd")){
                                writeInFile("ptd",  line);
                            }else if(line.contains("@cox")){
                                writeInFile("cox",  line);
                            }
                            else if(line.contains("@talktalk.")){
                                writeInFile("talktalk",  line);
                            }
                            else if(line.contains("@optimum.")){
                                writeInFile("optimum",  line);
                            }
                            else if(line.contains("@centurylink.")){
                                writeInFile("centurylink",  line);
                            }
                            else if(line.contains("@suddenlink.")){
                                writeInFile("suddenlink",  line);
                            }
                            else if(line.contains("@office.")){
                                writeInFile("office",  line);
                            }
                            else if(line.contains("@cableone.")){
                                writeInFile("cableone",  line);
                            }

                            else if(line.contains("@orange.")){
                                writeInFile("orange",  line);
                            }
                            else if(line.contains("@earthlink.")){
                                writeInFile("earthlink",  line);
                            }
                            else if(line.contains("@hotmail.")){
                                writeInFile("hotmail",  line);
                            }
                            else if(line.contains("@gmail.")){
                                writeInFile("gmail",  line);
                            }
                            else if(line.contains("@yahoo.")){
                                writeInFile("yahoo",  line);
                            }




                    }
                    System.out.println(j);
                    // Close the BufferedReader
                    reader.close();
                }

                System.out.println(i);
            }
        } catch (MalformedInputException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void writeInFile(String fileName, String line) {
        try {
            String filePath = "C:\\Users\\babach\\Desktop\\temp_mounir_results\\"+fileName+".txt";
            boolean fileExists = Files.exists(Path.of(filePath));

            if(fileExists){
                Files.write(Path.of(filePath), (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
            else{
                Files.createFile(Path.of(filePath));
                Files.write(Path.of(filePath), (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}