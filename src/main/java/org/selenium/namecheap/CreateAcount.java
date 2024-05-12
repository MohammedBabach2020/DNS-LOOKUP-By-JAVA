package org.selenium.namecheap;

import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



public class CreateAcount extends  Thread    {

    String email ;

    public CreateAcount(String email) {
        this.email = email;
    }

    public void run() {


        try {
            browsing();
        } catch (InterruptedException | GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

    }
    public   static  void  fiElem(WebDriver driver ,String xpath,String value) {

        WebElement element =    driver.findElement(By.xpath(xpath));
        element.sendKeys(value);
    }


    private void browsing() throws InterruptedException, GeneralSecurityException, IOException {

        WebDriver driver = null;
        String[] account_params = new String[0];
        try {


            account_params = new String[4];


            driver = new FirefoxDriver();
            driver.get("https://www.sandbox.namecheap.com/myaccount/signup/");

            JavascriptExecutor js = (JavascriptExecutor) driver;


            Faker faker = new Faker();
            String firstname = faker.name().firstName();
            String username = firstname + new Random().nextInt(1000);
            //username
            fiElem(driver, "/html/body/form[1]/div[3]/div/div/div/div/ul/li/fieldset/div[3]/input", username);
            account_params[0] = username;

            //password
            String password = faker.name().firstName() + faker.number().numberBetween(1, 100000);
            fiElem(driver, "/html/body/form[1]/div[3]/div/div/div/div/ul/li/fieldset/div[5]/input[1]", password);
            account_params[1] = password;

            //password confirmation
            fiElem(driver, "/html/body/form[1]/div[3]/div/div/div/div/ul/li/fieldset/div[6]/input[1]", password);
            //first name
            fiElem(driver, "/html/body/form[1]/div[3]/div/div/div/div/ul/li/fieldset/div[7]/input", firstname);
            //last name
            fiElem(driver, "/html/body/form[1]/div[3]/div/div/div/div/ul/li/fieldset/div[8]/input", faker.name().lastName());
            //email
            fiElem(driver, "/html/body/form[1]/div[3]/div/div/div/div/ul/li/fieldset/div[9]/input", this.email);
            sleep(800);
            account_params[2] = this.email;
            driver.findElement(By.xpath("/html/body/form[1]/div[3]/div/div/div/div/ul/li/fieldset/div[12]/input")).click();

            driver.get("https://ap.www.sandbox.namecheap.com/settings/tools/apiaccess/");
            sleep(800);
            js.executeScript("document.getElementById('enableApiAccessToggle').click()");


            sleep(800);
            driver.findElement(By.cssSelector("[placeholder='Namecheap Password']")).sendKeys(password);


            // confirm password
            List<WebElement> elements = driver.findElements(By.tagName("button"));
            WebElement Confirm = null;
            for (WebElement e : elements) {

                if (e.getText().equalsIgnoreCase("confirm")) {
                    Confirm = e;
                }
            }

            assert Confirm != null;
            Confirm.click();
            sleep(800);

            // go to whitelist ip page
            elements = driver.findElements(By.tagName("a"));
            WebElement whiteList = null;
            for (WebElement e : elements) {
                if (e.getText().equalsIgnoreCase("edit")) {
                    whiteList = e;
                }
            }
            assert whiteList != null;
            whiteList.click();

            //click on add ip button

            elements = driver.findElements(By.tagName("button"));
            WebElement addIp = null;
            for (WebElement e : elements) {

                if (e.getText().equalsIgnoreCase("add ip")) {
                    addIp = e;
                }
            }
            assert addIp != null;
            addIp.click();
            //fill password input
            driver.switchTo().activeElement().sendKeys(password);
            //fill the ip input
            driver.findElement(By.cssSelector("[placeholder='000.000.000.000']")).sendKeys("66.45.250.142");
            sleep(500);
            // save changes
            elements = driver.findElements(By.tagName("button"));
            WebElement SaveChanges = null;
            for (WebElement e : elements) {

                if (e.getText().equalsIgnoreCase("save changes")) {
                    SaveChanges = e;
                }
            }
            assert SaveChanges != null;
            SaveChanges.click();


            //done from whiteList
            elements = driver.findElements(By.tagName("a"));
            WebElement done = null;
            for (WebElement e : elements) {

                if (e.getText().equalsIgnoreCase("done")) {
                    done = e;
                }
            }
            assert done != null;
            done.click();

            sleep(800);
            account_params[3] = driver.findElement(By.className("gb-text-break")).getText();

            //  done from account preferences
            elements = driver.findElements(By.tagName("a"));
            for (WebElement e : elements) {

                if (e.getText().equalsIgnoreCase("done")) {
                    done = e;
                }
            }

            done.click();
            AppendValues ap = new AppendValues(account_params);
            ap.append();
            driver.quit();
        } catch (ElementClickInterceptedException | NoSuchElementException | JavascriptException ex) {
            Files.write(Path.of("C:\\Users\\dev_team\\Desktop\\folder\\logs.txt"), (Arrays.toString(account_params) + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            driver.quit();
            throw new RuntimeException();
        } catch (Exception ex) {
            Files.write(Path.of("C:\\Users\\dev_team\\Desktop\\folder\\logs.txt"), (Arrays.toString(account_params) + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            driver.quit();
            throw new RuntimeException();
        }
    }



}
