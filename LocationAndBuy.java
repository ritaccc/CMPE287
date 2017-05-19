package scenarios;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
public class LocationAndBuy {
    public AndroidDriver driver;
    String app_package_name = "com.groupon:id/";
    int waitTime=2;
    @BeforeTest
    public void ConnectAppium() throws Exception {
        //Environment settings
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.0");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "5.1");
        capabilities.setCapability("deviceName", "AndroidDevice");
        capabilities.setCapability("app", "/Users/tubaozi/Desktop/CMPE287_Ass3/com.groupon.apk");
        capabilities.setCapability("appPackage", "com.groupon");
        capabilities.setCapability("appActivity", "com.groupon.home.main.activities.Carousel");
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }
    @Test
    public void buyingTest() throws MalformedURLException, InterruptedException {
        System.out.println("----------Driver has been connected, and start testing procedures");
        (new WebDriverWait(driver, waitTime))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.id("android:id/content"))).click();

         /* Test choosing countries */
        try{
            System.out.println("----------Start choosing country");
            (new WebDriverWait(driver, waitTime))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.id(app_package_name+"try_another_city"))).click();
            (new WebDriverWait(driver, waitTime))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.id(app_package_name+"action_bar_search_edittext"))).sendKeys("United States");
            (new WebDriverWait(driver, waitTime))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.id(app_package_name+"billing_address_list_item")));
            List<WebElement> countryList=driver.findElementsById(app_package_name+"billing_address_list_item");
            countryList.get(0).click();
        }
        catch (Exception e){
            System.out.println("No need to select countries");
        }
        /*Test choosing cities*/
        try {
            System.out.println("----------Start choosing city");
            (new WebDriverWait(driver, waitTime))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.id(app_package_name+"action_bar_search_edittext"))).sendKeys("San Jose");
            (new WebDriverWait(driver, waitTime))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.id(app_package_name+"search_suggestions_list_item")));
            List<WebElement> cityList=driver.findElementsById(app_package_name+"search_suggestions_list_item");
            cityList.get(0).click();

        }catch (Exception e){
            System.out.println("No need to select cities");
        }


        /*input username and password*/
        try{
            System.out.println("----------Start sign in");
            (new WebDriverWait(driver, waitTime))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.id(app_package_name+"done"))).click();

            try{
                System.out.println("Input user information");
                (new WebDriverWait(driver, waitTime))
                        .until(ExpectedConditions.presenceOfElementLocated(
                                By.id(app_package_name+"fragment_log_in_sign_up_email"))).sendKeys("CMPE287team2@gmail.com");
                driver.findElementById(app_package_name+"fragment_log_in_sign_up_password").sendKeys("tester123");
                driver.findElementById(app_package_name+"fragment_log_in_sign_up_groupon_button").click();
            }catch (Exception e){
                System.out.println("User information has been saved, and login automatically");
            }
        }catch (Exception e){
            System.out.println("It cannot be signed in");
        }

        /*select "Beauty" Category*/
        try{
            System.out.println("----------find items of beauty");
            (new WebDriverWait(driver, waitTime))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.id(app_package_name+"tile_navigation_image_view")));
            List<WebElement> bars = driver.findElementsById(app_package_name+"tile_navigation_image_view");
            int barNum=randomVal(bars.size()-1,0);
            bars.get(2).click();

            //Randomly pick up one item
            try{
                (new WebDriverWait(driver, waitTime))
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.id(app_package_name+"deal_image")));
                List<WebElement> productImages=driver.findElementsById(app_package_name+"deal_image");
                System.out.println("****The items number of beauty is:"+productImages.size());

                int randomImage=randomVal(productImages.size()-1,0);
                productImages.get(randomImage).click();
                //Situation1  --different type of items
                try{
                    System.out.println("deal_card_content check");
                    (new WebDriverWait(driver, waitTime))
                            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                    By.id(app_package_name+"deal_card_content")));
                    List<WebElement> selections = driver.findElementsById(app_package_name+"deal_card_content");
                    int randomNum = randomVal(selections.size()-1,0);
                    selections.get(randomNum).click();
                }catch(Exception e){
                    System.out.println("only one deal_card");
                }

                //Situation2 --differnt options, such as size or color
                try{
                    System.out.println("start checking selections");
                    (new WebDriverWait(driver, waitTime))
                            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                    By.id(app_package_name+"selected_variation")));
                    List<WebElement> options=driver.findElementsById(app_package_name+"selected_variation");
                        //open the container of options
                        System.out.println("You should select some options such as color and size");
                        options.get(0).click();
                        WebElement option1=driver.findElementById(app_package_name+"bottom_sheet_container");
                        /*expand all the options_expand*/
                        List<WebElement> expandOptions=option1.findElements(
                                By.id(app_package_name+"inline_options_expand"));
                        for(int i=1;i<expandOptions.size();i++){
                            expandOptions.get(i).click();
                        }
                        //Select options
                        List<WebElement> variationContainer=option1.findElements(
                                By.id(app_package_name+"variations_recycler_layout"));
                        for(int i=0;i<expandOptions.size();i++){
                            List<WebElement> variations=variationContainer.get(i).findElements(
                                    By.id(app_package_name+"inline_option_overlay"));
                            for(int j=0;j<variations.size();j++){
                                WebElement temp=variations.get(j);
                                if(temp.isDisplayed() && temp.isEnabled()){
                                    temp.click();
                                    break;
                                }
                            }
                        }
                    //close the container of options
                        option1.findElement(By.id(app_package_name+"inline_options_bottom_sheet_close_button")).click();
                }catch (Exception e){
                    System.out.println("There is no need to select parameters");
                }
                System.out.println("----------start clicking buy");
                //click buy
                try{
                    (new WebDriverWait(driver, waitTime))
                            .until(ExpectedConditions.presenceOfElementLocated(
                                    By.id(app_package_name+"buy_button"))).click();
                }catch (Exception e){
                    System.out.println("No buy button found");
                }
                /*Select quantity*/
                try{
                    System.out.println("----------Start change the quantity");
                    (new WebDriverWait(driver, waitTime))
                            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                    By.id(app_package_name+"deal_card_crystal")));
                    List<WebElement> cartItems=driver.findElementsById(app_package_name+"deal_card_crystal");
                    for(int i=0;i<cartItems.size();i++){
                        cartItems.get(i).findElement(By.id(app_package_name+"crystal_quantity_text")).click();
                        List<WebElement> qualityOptions=driver.findElementsById("android:id/text1");
                        int qualityOption=randomVal(qualityOptions.size()-1,0);
                        qualityOptions.get(qualityOption).click();
                    }
                } catch (Exception e){
                    System.out.println("The quantity can not be changed");
                }
                /*Check the final subtotal*/
                System.out.println("--------start check the final subtotal");
                double expectedSubtotal=0;
                try{
                    (new WebDriverWait(driver, waitTime))
                            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                    By.id(app_package_name+"crystal_quantity_text")));
                    List<WebElement> quantityNumElement=driver.findElementsById(app_package_name+"crystal_quantity_text");

                    (new WebDriverWait(driver, waitTime))
                            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                    By.id(app_package_name+"crystal_deal_card_price")));
                    List<WebElement> priceElement=driver.findElementsById(app_package_name+"crystal_deal_card_price");

                    if(priceElement.size()==quantityNumElement.size()){
                        for(int i=0;i<quantityNumElement.size();i++){
                            expectedSubtotal=expectedSubtotal
                                    +Integer.parseInt(quantityNumElement.get(i).getText())
                                    *valueFormat(priceElement.get(i).getText());
                        }
                    }
                }catch (Exception e){
                    System.out.println("No items selected, or the number of prices and items are different");
                }
                //check value of the result
                (new WebDriverWait(driver, waitTime))
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.id(app_package_name+"purchase_crystal_label_value")));
                List<WebElement> valueElements=driver.findElementsById(app_package_name+"purchase_crystal_label_value");

                float expectedFinalTotal=0;
                for(int i=0;i<valueElements.size()-1;i++){
                    expectedFinalTotal+=valueFormat(valueElements.get(i).getText());
                }
                float actualSubtotal=valueFormat(valueElements.get(0).getText());
                float actualFinalTotal=valueFormat(valueElements.get(valueElements.size()-1).getText());

                //The Assert Equations
                Assert.assertEquals(expectedFinalTotal,actualFinalTotal,0.01);
                Assert.assertEquals(expectedSubtotal,actualSubtotal,0.01);

            }catch(Exception e){
                System.out.println("No items found");
            }
        }catch (Exception e){
            System.out.println("No beauty item");
        }
    }
    //randomly select a value between max and min
    public int randomVal(int max, int min){
        Random rand=new Random();
        return rand.nextInt(max-min+1) + min;
    }

    //Convert String with $ to integer,eg:$-5->-5
    public float valueFormat(String str){
        System.out.println(str);
        if(str.equals(null) || str.length()==0 || str.equals("FREE")) return 0;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)!='$'){
                sb.append(str.charAt(i));
            }
        }
        return Float.parseFloat(sb.toString());
    }
    @AfterTest
    public void teardown() {
        driver.quit();
    }
}
