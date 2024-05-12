package org.selenium.namecheap;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;


class Main  {

     public static  void main(String[] args)  {

         try {
             String filePath = "C:\\Users\\dev_team\\Desktop\\folder\\possibilitiess.txt";
             BufferedReader reader = Files.newBufferedReader(Path.of(filePath));

             // Loop over lines in the file
             String line;
             int i = 0;
             Set<Thread> threadSet = new HashSet<>();
             while ((line = reader.readLine()) != null) {
                 i++;
                 CreateAcount runner = new CreateAcount(line +"@gmail.com");
                 runner.start();
                 threadSet.add(runner);
                 if (i % 10 == 0) {

                     boolean thereIsAsthreadworking = true;
                    while(thereIsAsthreadworking){
                        thereIsAsthreadworking =false;
                        for (Thread t : threadSet) {
                            if (t.isAlive()) {
                                thereIsAsthreadworking = true;

                            }
                        }
                    }

                     threadSet.clear();
                 }


             }

             // Close the BufferedReader
             reader.close();
         }
         catch( IOException ex ){

             ex.printStackTrace();
         }






 //   CreateAcount newAccount = new CreateAcount();

       // newAccount.start();
    }



/*

    int howmany = 0;
        Set<String> set = new HashSet<String>();
        while (howmany<3000){
            String  str= "azertyuiopqsdfghjklmwxcvbn2024";

            StringBuilder sb = new StringBuilder(str);

            int howManyPoint =new Random().nextInt(1 ,str.length());
            ArrayList<Integer> usedIndexes = new ArrayList<Integer>();
            for(int i=0; i<howManyPoint;i++){
                int index =new Random().nextInt(1 ,str.length());
                if(!usedIndexes.contains(index)){
                    if(sb.charAt(index-1) != '.'    && sb.charAt(index) != '.' ){
                        sb.insert(index, '.');
                    }
                    usedIndexes.add(index);
                }
            }
            howmany++;
            set.add(sb.toString());
            System.out.println("for now there is : " + set.size() +" of possibility");
            System.out.println(howmany);

        }



        Files.createFile(Path.of("C:\\Users\\dev_team\\Desktop\\folder\\possibilitiess.txt"));

        for(int i = 0 ; i < set.toArray().length ;i++){
            Files.write(Path.of("C:\\Users\\dev_team\\Desktop\\folder\\possibilitiess.txt"), (set.toArray()[i] + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }




 */

}

