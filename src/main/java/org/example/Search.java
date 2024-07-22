package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class Search {
    ChromeDriver chromeDriver;

    @BeforeClass
    public void setupClass() {
        WebDriverManager.chromedriver().setup();
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        chromeDriver.get("http://localhost:3000/");

        // Login
        WebElement loginIcon = chromeDriver.findElement(By.cssSelector("i.fas.fa-user.fa-2x"));
        loginIcon.click();
        sleep(2000);

        WebElement mailField = chromeDriver.findElement(By.id("email"));
        WebElement passField = chromeDriver.findElement(By.id("password"));
        WebElement loginButton = chromeDriver.findElement(By.xpath("//button[@type='submit']"));

        mailField.sendKeys("imhiee06@gmail.com");
        sleep(1000);
        passField.sendKeys("Hau2002@");
        loginButton.click();

        sleep(2000);

        //chuyen den trang sản phẩm
        WebElement productLink = chromeDriver.findElement(By.cssSelector(".nav-item.nav-link"));
        productLink.click();

        sleep(2000);
    }

    @BeforeMethod
    public void setup() {
        // Dam bao ơ trang sp trc moi case
        chromeDriver.get("http://localhost:3000/product");
        sleep(2000);
    }

    @Test
    public void testValidKeyword() {
        performSearch("m");
        verifySearchResults("m");
    }

    @Test
    public void testEmptyKeyword() {
        performSearch("");
        checkmessage("Vui lòng nhập từ khóa để tìm kiếm");
    }

    @Test
    public void testspaceKeyword() {
        performSearch(" ");
        checkmessage("Vui lòng nhập từ khóa để tìm kiếm");
    }

    @Test
    public void testSpecialCharacterSearchKeyword() {
        performSearch("!@#$%^&*");
        checkmessage("Từ khóa không chứa kí tự đặc biệt");
    }

    @Test
    public void testCaseInsensitiveSearchKeyword() {
        performSearch("mA Quỷ");
        verifySearchResults("mA Quỷ");
    }
      //thực hiện tìm kiếm với keyword
    private void performSearch(String keyword) {
        WebElement searchField = chromeDriver.findElement(By.cssSelector("input.form-control.p-3[placeholder='Tìm kiếm']"));
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(searchField).click().sendKeys(keyword).perform();

        // Chờ gợi ý tìm kiếm hiển thị
        sleep(2000);
    }

    private void verifySearchResults(String keyword) {
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul > li > span")));
        List<WebElement> searchResults = chromeDriver.findElements(By.cssSelector("ul > li > span"));

        boolean expected = searchResults.size() > 0;
        boolean actual = false;
        for (WebElement result : searchResults) {
            if (result.getText().toLowerCase().contains(keyword.toLowerCase())) {
                actual = true;
                break;
            }
        }

        // Log expected and actual
        Reporter.log("Expected: " + expected);
        Reporter.log("Actual: " + actual);
        System.out.println("Expected: " + expected + ", Actual: " + actual);

        // Ensure test passes but log failure
        Assert.assertTrue(actual, "No products displayed that match the search keyword!");
    }

//ktra kqua tìm kiếm
    private void verifyNoSearchResults() {
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul > li > span")));
        List<WebElement> searchResults = chromeDriver.findElements(By.cssSelector("ul > li > span"));

        boolean expected = searchResults.isEmpty();
        boolean actual = expected;

        // Log expected and actual
        Reporter.log("Expected: " + expected);
        Reporter.log("Actual: " + actual);
        System.out.println("Expected: " + expected + ", Actual: " + actual);

        // Ensure test passes but log failure
        Assert.assertTrue(actual, "Some products are displayed but none were expected!");
    }


    //ktra message
    private void checkmessage(String expectedMessage) {
        WebElement searchField = chromeDriver.findElement(By.cssSelector("input.form-control.p-3[placeholder='Tìm kiếm']"));
        String actuaMessage = searchField.getAttribute("value");

        //log expecd và atual
        Reporter.log("Expected message: " + expectedMessage);
        Reporter.log("Actual message: " + actuaMessage );
        System.out.println("Epected message: " + expectedMessage);

        Assert.assertEquals(actuaMessage, expectedMessage, "The message ko khớp");
    }
    @AfterMethod
    public void cleanup() {
        if (chromeDriver != null) {
//            chromeDriver.quit();
        }
    }

    @AfterClass
    public void cleanupClass() {
        // Cleanup code
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}