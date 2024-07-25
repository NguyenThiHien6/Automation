package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ChangePassword {
    ChromeDriver chromeDriver;
    @BeforeClass
    public void setupClass() {
        WebDriverManager.chromedriver().setup();
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        chromeDriver.get("http://localhost:3000/");
        //login
        WebElement loginIcon = chromeDriver.findElement(By.cssSelector("i.fas.fa-user.fa-2x"));
        loginIcon.click();
        sleep(2000);

        WebElement mailField = chromeDriver.findElement(By.id("email"));
        mailField.sendKeys("imhiee06@gmail.com");
        sleep(1000);
        WebElement passField = chromeDriver.findElement(By.id("password"));
        passField.sendKeys("Hau2002@");
        sleep(1000);

        WebElement buttonLogin = chromeDriver.findElement(By.xpath("//button[@type='submit']"));
        buttonLogin.click();
        sleep(2000);
        //chuyen den trang thong tin chứa đỏi pas
        WebElement thongtin = chromeDriver.findElement(By.cssSelector(".nav-item.nav-link[href='/thongTin']"));
        thongtin.click();
        sleep(2000);



    }
    @BeforeMethod
    public void setup() {
        //dambao ơ trag thong tin mtrc moi case
        chromeDriver.get("http://localhost:3000/thongTin");
        sleep(2000);

    }
    @Test
    public void CheckchageValid(){
        WebElement ChangeFeild = chromeDriver.findElement(By.id("left-tabs-example-tab-fourth"));
        ChangeFeild.click();
        sleep(2000);

        //nhập các truường
        WebElement oldPassField = chromeDriver.findElement(By.cssSelector("input[type='password']:nth-of-type(1)"));
        oldPassField.sendKeys("Hau2002@");
        sleep(1000);
        WebElement newPassField = chromeDriver.findElement(By.cssSelector("input[type='password']:nth-of-type(2)"));
        newPassField.sendKeys("Hien2002@");
        sleep(1000);
        WebElement confirmPass = chromeDriver.findElement(By.cssSelector("input[type='password']:nth-of-type(3)"));
        confirmPass.sendKeys("Hien2002@");
        sleep(1000);
        WebElement buttonSave = chromeDriver.findElement(By.xpath("//button[@type='submit']"));
        buttonSave.click();

       //  Sử dụng WebDriverWait để chờ thông báo xuất hiện
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(4));
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Đổi mật khẩu thành công']")));

        // Kiểm tra thông báo
        String actual = message.getText();
        String expect = "Đổi mật khẩu thành công";


        //so sánh
        Assert.assertEquals(actual,expect,  "Thông báo đổi mật khẩu thành công không khớp với mong đợi.");
        // Xóa các trường nhập dữ liệu
        newPassField.clear();
        newPassField.clear();
        confirmPass.clear();









    }
    @AfterMethod
    public void cleanup() {
        if (chromeDriver != null) {
//            chromeDriver.quit();
        }
    }

    @AfterClass
    public void cleanupClass() {

    }
    private void sleep(int time) {
        try {
            Thread.sleep(time);
        }catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
