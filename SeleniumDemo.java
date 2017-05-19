import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.support.ui.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.html.HTMLSelectElement;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

public class SeleniumDemo {
    private WebDriver driver;
    /** open chrome, close popup window **/
    @Before
    public void getChromedriver() {

        System.setProperty("webdriver.chrome.driver", "/Users/ritaccc/bin/chromedriver");
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--disable-notifications");

        Map<String, Object> prefs = new LinkedHashMap<>();
        prefs.put("credentials_enable_service", Boolean.valueOf(false));
        prefs.put("profile.password_manager_enabled", Boolean.valueOf(false));
        ops.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(ops);
        driver.get("http://www.groupon.com");
        /** Two methods to get rid of pop Sign-Up page **/
        /** Method-1: Click "No Thanks" **/
        WebElement waitPop = (new WebDriverWait(
                driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nothx")));
        waitPop.sendKeys(Keys.ENTER);
        //sign in
        driver.findElement(By.id("ls-user-signin")).sendKeys(Keys.ENTER);
        String email = "cmpe287team2@gmail.com";
        String psw = "tester123";
        driver.findElement(By.xpath("//*[@id=\"email-input\"]")).sendKeys(email);
        driver.findElement(By.xpath("//*[@id=\"password-input\"]")).sendKeys(psw);
        driver.findElement(By.xpath("//*[@id=\"signin-button\"]")).click();
        pauseFunc(2000);
    }
    @Test
    public void testSearchAndBuy() {
        //search iphone6
        WebElement element = driver.findElement(By.name("search"));
        element.sendKeys("iphone 6");
        element.submit();
        pauseFunc(4000);
        WebElement getResult = driver.findElement(By.xpath("//*[@id=\"pull-page-header-title\"]/div/span"));
        assertEquals(getResult.getText(), "iphone 6");

        int i = 1;
        WebElement content = driver.findElement(By.xpath("//*[@id=\"pull-cards\"]/" + getxpth(i)));
        while(!content.getText().startsWith("Apple iPhone 6/6s or 6 Plus/6s Plus (GSM Unlocked)")) {
            content = driver.findElement(By.xpath("//*[@id=\"pull-cards\"]/" + getxpth((++i))));
        }
        content.click();
        pauseFunc(4000);

        //choose color and size
        WebElement colorEle = driver.findElement(By.id("trait-0"));
        Select selColor = new Select(colorEle);
        selColor.selectByValue("Silver");
        WebElement sizeEle = driver.findElement(By.xpath("//*[@id=\"trait-1\"]"));
        Select selSize = new Select(sizeEle);
        selSize.selectByValue("iPhone 6 64GB");
        pauseFunc(4000);

        //click to buy
        WebElement buyEle = driver.findElement(By.id("buy-link"));
        buyEle.click();
        pauseFunc(4000);

        // modify quantity to one;
        WebElement quatity = driver.findElement(By.xpath("//*[@id=" +
                "\"items-list\"]/" +
                "div/div/div[2]/div/div[2]/select"));
        Select selectQuantity = new Select(quatity);
        selectQuantity.selectByVisibleText("1");

        // check the price
        WebElement finalPrice = driver.findElement(By.xpath("//*[@id" +
                "=\"items-list\"]/div/div/div[2]/div/div[1]/div[1]"));
        assertEquals(finalPrice.getText(), "$289.99");

        //check out
        WebElement checkOutEle = driver.findElement(By.xpath("//*[@id" +
                "=\"bottom-proceed-to-checkout\"]"));
        checkOutEle.click();
        pauseFunc(4000);
    }
    @Test
    public void referFriend() {
        Actions action = new Actions(driver);

        //hover on username and click refer to a friend
        WebElement userEle = driver.findElement(By.id("user-name"));
        assertEquals(userEle.getText(),"CMPE287Team2");
        action.moveToElement(userEle).moveToElement(driver.findElement
                (By.id("refer-a-friend"))).click().build().perform();;
        pauseFunc(2000);
        WebElement referByFB = driver.findElement(By.xpath("//*[@id=\"social\"]/li[3]/a"));
        assertEquals(referByFB.getText(), "Share it on Facebook");
        referByFB.sendKeys(Keys.ENTER);
        pauseFunc(2000);

        //switch to a new tab in chrome driver
        ArrayList<String> tabs = new ArrayList (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        //login in with facebook account
        String fbAcct = "cmpe287team2@gmail.com";
        String fbPsw = "tester123";
        driver.findElement(By.id("email")).sendKeys(fbAcct);
        driver.findElement(By.id("pass")).sendKeys(fbPsw);
        driver.findElement(By.id("u_0_2")).click();
        pauseFunc(4000);

        // locate on fb account name, and test if it is "Anlif James"
        WebElement accountName = driver.findElement(By.id(("u_0_1s")));
        assertEquals(accountName.getText(),"Anlif James");

        // post groupn on facebook
        WebElement post = driver.findElement(By.id("u_0_1w"));
        post.click();
        pauseFunc(2000);

        // switch back
        driver.switchTo().window(tabs.get(0));
        pauseFunc(4000);
    }
	@After
	public void quitDriver() {
		driver.quit();
	}
	private void pauseFunc(int millisec) {
        try {
            Thread.sleep(millisec);
        }catch (InterruptedException ex) {
            System.out.println("InterruptedException from Thread.sleep in main");
            ex.printStackTrace();
        }
    }
    public String getxpth(int i) {
        String path = Integer.toString(i);
        return "figure[" + path + "]/a/div/div[2]/div[1]";
    }

}
