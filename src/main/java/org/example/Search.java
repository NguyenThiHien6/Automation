package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
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
    @Test
    public void checkKeywordPart() {
        performSearch("tô bình yên vẽ hạnh");
        verifySearchResults("Tô Bình Yên Vẽ Hạnh Phúc (Tái Bản 2023)");
    }

      //lôi ElementNotInteractableException, nút icon ko hđ
     @Test
    public void testcheckIconSerch() {
            //nhap tu khoa
         String keyword = "7";
         WebElement searchField = chromeDriver.findElement(By.cssSelector("input.form-control.p-3[placeholder='Tìm kiếm']"));
         searchField.sendKeys(keyword);

         boolean iconClickedSuccessfully = false;
         try {
             WebElement iconSearch = chromeDriver.findElement(By.cssSelector("i.fa.fa-search"));

             // Nhấp vào biểu tượng tìm kiếm
             iconSearch.click();

             // Nếu không xảy ra lỗi, cho rằng biểu tượng tìm kiếm hoạt động đúng
             iconClickedSuccessfully = true;

         } catch (ElementNotInteractableException e) {
             // Xử lý lỗi nếu biểu tượng tìm kiếm không thể tương tác
             Reporter.log("Actual: Biểu tượng tìm kiếm không hoạt động.");
             System.out.println("Actual: Biểu tượng tìm kiếm không hoạt động.");
         } catch (Exception e) {
             // Xử lý các lỗi khác nếu cần
             Reporter.log("Lỗi không mong muốn: " + e.getMessage());
             System.out.println("Lỗi không mong muốn: " + e.getMessage());
         }

         // log messgage
//         Reporter.log("Expected: Biểu tượng tìm kiếm hoạt động đúng.");
//         Reporter.log("Actual: " + (iconClickedSuccessfully ? "Biểu tượng tìm kiếm đã hoạt động đúng." : "Biểu tượng tìm kiếm không hoạt động."));
         System.out.println("Expected: Biểu tượng tìm kiếm hoạt động đúng.");
         System.out.println("Actual: " + (iconClickedSuccessfully ? "Biểu tượng tìm kiếm đã hoạt động đúng." : "Biểu tượng tìm kiếm không hoạt động."));

         // Kiểm tra
         Assert.assertTrue(iconClickedSuccessfully, "Biểu tượng tìm kiếm không hoạt động như mong đợi.");
     }



    //thực hiện tìm kiếm với keyword
    private void performSearch(String keyword) {
        WebElement searchField = chromeDriver.findElement(By.cssSelector("input.form-control.p-3[placeholder='Tìm kiếm']"));
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(searchField).click().sendKeys(keyword).perform();

        // Chờ gợi ý tìm kiếm hiển thị
        sleep(2000);
    }

        //check sp có chứa keyword
    private void verifySearchResults(String keyword) {
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul > li > span")));
        List<WebElement> searchResults = chromeDriver.findElements(By.cssSelector("ul > li > span"));

        boolean allResultsContainKeyword = true;
        for (WebElement result : searchResults) {
            if (!result.getText().toLowerCase().contains(keyword.toLowerCase())) {
                allResultsContainKeyword = false;
                break;
            }
        }

        // Log expected và actual
        Reporter.log("Expected: KQ chứa keyword.");
        Reporter.log("Actual: " + allResultsContainKeyword);
        System.out.println("Expected:  Tất cả kết quả chứa keyword. Actual: " + allResultsContainKeyword);

        //
        Assert.assertTrue(allResultsContainKeyword, "Một hoặc nhiều sản phẩm không chứa từ khóa được tìm kiếm!");

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