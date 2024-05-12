package org.selenium.namecheap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.util.concurrent.locks.ReentrantLock;

public class AppendValues {
    private final  String[] params ;
    private final ReentrantLock lock = new ReentrantLock();
    public AppendValues(String[] params) {
        this.params = params;
    }




    protected void append() throws GeneralSecurityException, IOException {
        lock.lock();

     String line = this.params[0] + ";"  +    this.params[1] +    ";"     +   this.params[2] +";"  +   this.params[3] +";";

        Files.write(Path.of("C:\\Users\\dev_team\\Desktop\\folder\\accounts.txt"), (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        lock.unlock();
    }

}
