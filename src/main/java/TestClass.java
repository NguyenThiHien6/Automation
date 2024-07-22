import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestClass {

    ChromeDriver chromeDriver;

    @BeforeClass
    public void setupClass() {
        System.out.println("Before class");
        WebDriverManager.chromedriver().setup();

        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
    }

    @BeforeMethod
    public void setup() {
        chromeDriver.get("http://localhost:3000/");
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password, boolean expectedLogin) {
        sleep(3000); // Chờ 3 giây để trang tải
        performLogin(email, password);
        sleep(2000); // Chờ 2 giây để trang xử lý đăng nhập

        if (expectedLogin) {
            // Đăng nhập thành công
            Assert.assertTrue(isLoggedIn(), "Đăng nhập thành công");
            performLogout();
        } else {
            // Kiểm tra đăng nhập không thành công
            WebElement errorMessage;
            try {
                errorMessage = chromeDriver.findElement(By.xpath("//p[@style='color: red;']"));
            } catch (NoSuchElementException e) {
                errorMessage = null;
            }

            if (errorMessage != null) {
                String actualErrorMessage = errorMessage.getText().trim();
                String expectedErrorMessage = "Đăng nhập không thành công";
                Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Thông báo lỗi không khớp do đăng nhập không hợp lệ");
            } else {
                Assert.fail("Không tìm thấy thông báo lỗi khi đăng nhập không thành công");
            }

            // Xóa cookies
            chromeDriver.manage().deleteAllCookies();
            chromeDriver.get("http://localhost:3000/");
        }
    }


    @AfterMethod
    public void cleanup() {
        System.out.println("After method");
        chromeDriver.manage().deleteAllCookies();
        chromeDriver.get("about:blank");
    }

    @AfterClass
    public void cleanupClass() {
        System.out.println("After class");
//        chromeDriver.quit();
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"imhiee06@gmail.com", "Hau2002@", true}, // Đăng nhập hợp lệ
                {"invalidemail@example.com", "Hau2002@", false}, // Email không hợp lệ
                {"imhiee06@gmail.com", "invalidpassword", false} // Mật khẩu không hợp lệ
                // Thêm dữ liệu kiểm thử nếu cần
        };
    }

    private void performLogin(String email, String password) {
        sleep(1000); // Chờ 1 giây để đảm bảo phần tử đã tải xong
        WebElement userIcon = chromeDriver.findElement(By.cssSelector("i.fas.fa-user.fa-2x"));
        userIcon.click();
        sleep(1000); // Chờ 1 giây sau khi click để đảm bảo phần tử tiếp theo đã sẵn sàng
        WebElement emailField = chromeDriver.findElement(By.id("email"));
        emailField.sendKeys(email);
        WebElement passwordField = chromeDriver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        WebElement loginButton = chromeDriver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();
    }

    private void performLogout() {
        WebElement iconDX = chromeDriver.findElement(By.cssSelector("i.fas.fa-power-off.fa-2x"));
        iconDX.click();
        sleep(1000); // Thêm thời gian chờ sau khi nhấn đăng xuất để đảm bảo hệ thống xử lý
        WebElement userIcon = chromeDriver.findElement(By.cssSelector("i.fas.fa-user.fa-2x"));
        userIcon.click(); // Nhấn vào icon người dùng sau khi đăng xuất
        sleep(1000); // Thêm thời gian chờ sau khi nhấn để đảm bảo phần tử tiếp theo đã sẵn sàng
    }

    private boolean isLoggedIn() {
        try {
            WebElement iconDX = chromeDriver.findElement(By.cssSelector("i.fas.fa-power-off.fa-2x"));
            return iconDX.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
