import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LinkChecking
{
    /*  protected static ChromeDriver driver; */
    WebDriver driver = null;
    String URL = "http://uitestingplayground.com";
    //String URL = "https://coschedule.com/marketing-terms-definitions/meta-title";
    public static String status = "passed";
    String username = "user-name";
    String access_key = "access-key";

    String url = "";
    HttpURLConnection urlconnection = null;
    int responseCode = 200;
    private Object WebDriverManager;

    @BeforeClass
    public void testSetUp() throws MalformedURLException {

        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void test_Selenium_Broken_Links() throws InterruptedException, SocketException {
        driver.navigate().to(URL);
        driver.manage().window().maximize();
        List<WebElement> links = driver.findElements(By.tagName("a"));
        Iterator<WebElement> link = links.iterator();
        List<WebElement> metas = driver.findElements(By.cssSelector("meta[name*=\"title\"], meta[property*=\"title\"]"));
        List<WebElement> metaDesc = driver.findElements(By.cssSelector("meta[name*=\"desc\"], meta[property*=\"desc\"]"));
        //List<WebElement> metas = driver.findElements(By.xpath("//head//meta[@name=\"title\""));
        if(metas.size() > 0 || metaDesc.size() > 0){
            System.out.println("There are valid meta tags total number: " + metas.size()+metaDesc.size());
        }
        else
            System.out.println("No valid meta tags available");

        int valid_links = 0;
        int broken_links = 0;

        while (link.hasNext())
        {
            url = link.next().getAttribute("href");
            System.out.println(url);

            if ((url == null) || (url.isEmpty()))
            {
                System.out.println("URL is either not configured for anchor tag or it is empty");
                continue;
            }

            try {
                urlconnection = (HttpURLConnection) (new URL(url).openConnection());
                urlconnection.setRequestMethod("HEAD");
                urlconnection.connect();
                responseCode = urlconnection.getResponseCode();
                if (responseCode >= 400)
                {
                        System.out.println(url + " is a broken link");
                        broken_links++;
                }
                else
                {
                    System.out.println(url + " is a valid link");
                    valid_links++;
                }
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Detection of broken links completed with " + broken_links + " broken links and " + valid_links + " valid links\n");
    }

    @AfterClass
    public void tearDown()
    {
        if (driver != null) {
            driver.quit();
        }
    }
}