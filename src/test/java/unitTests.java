import org.junit.Test;
import org.selenium.namecheap.CreateAcount;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class unitTests {
    @Test
    public  void test(){

        try {
            String filePath = "C:\\Users\\dev_team\\Desktop\\folder\\possibilitiess.txt";
            BufferedReader reader = Files.newBufferedReader(Path.of(filePath));

            // Loop over lines in the file
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                i++;
                CreateAcount runner = new CreateAcount(line +"@gmail.com");
                runner.start();
         

            }

            // Close the BufferedReader
            reader.close();
        }
        catch( IOException ex ){

            ex.printStackTrace();
        }
    }
}
