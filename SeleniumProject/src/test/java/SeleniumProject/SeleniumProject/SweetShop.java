package SeleniumProject.SeleniumProject;

import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SweetShop {
	    WebDriver driver;
	    
	    String sweetText;
	    float Individualcost;
	    
	    @BeforeClass
	     public void launchBrowser() {
	    	 System.out.println("Launching browser");
	     }
		
		@BeforeTest
		public void setup() {
            System.setProperty(
            "webdriver.chrome.driver",
            "C:\\Users\\monishak\\Downloads\\chromedriver-win32\\chromedriver-win32\\chromedriver.exe");
            // Instantiate a ChromeDriver class.
            driver = new ChromeDriver();
            // Maximize the browser
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
		 }
	
		@AfterClass
		public void teardown() {
			driver.quit();
		}
		
		@Test(priority=0)
		public void sweetPurchase() {
		
        // Launch Website
        driver.get("https://sweetshop.netlify.app/ ");
        
        // Click on Browse Sweets
        driver.findElement(By.xpath("//a[text()='Browse Sweets']")).click();
     
        //select any 4 sweets
        List<WebElement> listofsweets= driver.findElements(By.xpath("(//h4[@class=\"card-title\"])"));
        	 System.out.println(listofsweets.size());

             for (WebElement sweet : listofsweets) {
                 String sweetname = sweet.getText();
                 System.out.println(sweetname);}
                 for(int i=13; i<=listofsweets.size(); i++) {
        sweetText = driver.findElement(By.xpath("(//h4[@class=\"card-title\"])["+i+"]")).getText();
        driver.findElement(By.xpath("(//div[@class=\"card-footer\"])["+i+"]")).click();
        String actualcost = driver.findElement(By.xpath("(//small[@class=\"text-muted\"])["+i+"]")).getText();
		Individualcost = Float.parseFloat(actualcost.substring(1));
		System.out.println(sweetText +": "+ Individualcost);
        }
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //Click on basket to check selected sweets are in basket
        WebElement pageTop = driver.findElement(By.xpath("//div[@class='container']"));
        js.executeScript("arguments[0].scrollIntoView();", pageTop);
        driver.findElement(By.xpath("//a[text()=' Basket']")).click();
        String text = driver.findElement(By.id("basketItems")).getText();
        Assert.assertTrue(text.contains("Nerds"));
        Assert.assertTrue(text.contains("Bubbly"));
        Assert.assertTrue(text.contains("Raspberry Drumstick"));
        Assert.assertTrue(text.contains("Dolly Mixture"));  
        System.out.println("Hence selected products are successfully addedin cart");
        
       }
		
		@Test(dependsOnMethods={"sweetPurchase"},priority=1)
		public void ValidateSweetPrice() {
			for(int i=1;i<=4;i++) {
			String quantity = driver.findElement(By.xpath("(//small[@class=\"text-muted\"])["+i+"]")).getText();
			int n = Integer.parseInt(quantity.substring(2));
			int a=i+1;
			String cost = driver.findElement(By.xpath("(//span[@class=\"text-muted\"])["+a+"]")).getText();
			
			float costprice = Float.parseFloat(cost.substring(1));
			System.out.println("Displayed price of item " +i+ " "+costprice);
			if(n*costprice==costprice){
			System.out.println("Price is calculated`correctly for each item");}
			costprice+=costprice;
			String total = driver.findElement(By.xpath("//li/span[contains(text(),'Total')]/following-sibling::strong")).getText();
			float totalPrice = Float.parseFloat(total.substring(1));
			if(totalPrice==costprice) {
				System.out.println("Total is validated");}
			
			}
			}
			
		@Test(dependsOnMethods={"sweetPurchase"},priority=2)
		public void EnterDetails(){
			driver.findElement(By.xpath("//label[text()=\"First name\"]/following-sibling::input[@id=\"name\"]")).sendKeys("Monisha");
			driver.findElement(By.xpath("//label[text()=\"Last name\"]/following-sibling::input[@id=\"name\"]")).sendKeys("Kamaraj");
			driver.findElement(By.id("email")).sendKeys("monishak@nousinfo.com");
			driver.findElement(By.id("address")).sendKeys("ABC Street-1");
			driver.findElement(By.id("country")).click();
			String option="United Kingdom";
			WebElement dropdown =driver.findElement(By.xpath("//select/option[contains(text(), '"+option+"')]"));
            dropdown.click();
            driver.findElement(By.id("city")).click();
            String Cityoption="Cardiff";
			WebElement citydropdown =driver.findElement(By.xpath("//select/option[contains(text(), '"+Cityoption+"')]"));
			citydropdown.click();
			driver.findElement(By.id("zip")).sendKeys("123"); //cc-name
			driver.findElement(By.id("cc-name")).sendKeys("MonishaKamaraj");
			driver.findElement(By.id("cc-number")).sendKeys("1234456678");
			driver.findElement(By.id("cc-expiration")).sendKeys("02/25");
			driver.findElement(By.id("cc-cvv")).sendKeys("1");
			}
		
		@Test(dependsOnMethods={"sweetPurchase"},priority=3)
		public void standardShipping() {
			String total = driver.findElement(By.xpath("//li/span[contains(text(),'Total')]/following-sibling::strong")).getText();
			float totalPrice = Float.parseFloat(total.substring(1));
			WebElement standardShipping = driver.findElement(By.xpath("//input[@id=\"exampleRadios2\"]/following::label"));
			final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(standardShipping));
			String sscost = standardShipping.getText();
			//standardShipping.click();
			float ss = Float.parseFloat(sscost.substring(20,23));
			System.out.println("Price after standard shipping:" +(ss+totalPrice));
			driver.findElement(By.xpath("//button[@type=\"submit\" and text()=\"Continue to checkout\"]")).click();
			System.out.println("Order is placed successfully");
		}
		

}
