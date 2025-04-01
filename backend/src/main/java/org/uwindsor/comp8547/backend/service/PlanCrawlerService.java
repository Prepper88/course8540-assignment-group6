package org.uwindsor.comp8547.backend.service;

import jakarta.annotation.PostConstruct;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Service
public class PlanCrawlerService {

    //automatically run the crawler functions for different providers to get plans
    @PostConstruct
    public void onStartup() {
        new Thread(() -> {
            try {
                TekSavvyCrawler();
                RogersCrawler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //-------------------scrape plans for teksavvy---------------------------------
    public void TekSavvyCrawler() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver/chromedriver.exe");


        WebDriver driver = new ChromeDriver();

        //initialize driver to the home page of the website
        driver.get("https://www.teksavvy.com/");

        //waiting for cookie window
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try{
            WebElement cookies = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("c-p-bn")));
            cookies.click();
        }
        catch(Exception e){}


        //open drop down menu and scrape internet options
        WebElement shop1 = driver.findElement(By.xpath("/html/body/div[2]/div/nav/ul[1]/li[1]/a"));
        shop1.click();
        Thread.sleep(700);
        //retrieve all elements in the same list
        List<WebElement> internetOptions = driver.findElements(By.xpath("/html/body/div[2]/div/nav/ul[1]/li[1]/div/div/div[2]/ul/li"));
        JSONObject whole = new JSONObject();    //json object to store whole information
        JSONArray options = new JSONArray();
        //save option text in the json object by a loop
        for (WebElement option : internetOptions) {
            String optionText = option.findElement(By.className("link-text")).getText();
            String description = option.findElement(By.tagName("p")).getText();
            options.put(optionText);
            options.put(description);
        }
        whole.put("internet options available", options);
        shop1.click();

        //find and click Check Availability button
        WebElement availability = driver.findElement(By.xpath("//*[@id=\"packages\"]/div[3]/div/div/a"));
        Thread.sleep(434);
        availability.click();
        Thread.sleep(534);

        //find address input box
        WebElement addr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("q")));
        Thread.sleep(334);

        //input the address in the form
        addr.sendKeys("11 Jeffrey St, Barrie");
        Thread.sleep(534);

        //waiting for dropdown manu of addresses corresponding to the input
        WebElement addrItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("selected")));
        Thread.sleep(334);

        //click on first item of the dropdown manu
        addrItem.click();
        Thread.sleep(512);

        //find and click Check Availability button to check internet plans for this address
        WebElement submit = driver.findElement(By.id("qualification-autocomplete-submit"));
        Thread.sleep(1334);
        submit.click();
        Thread.sleep(1365);

        //switch from popular plans to all plans to get whole data
        WebElement allPlans = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"sl-tab-3\"]")));
        Thread.sleep(1334);
        allPlans.click();
        Thread.sleep(1965);


        //plans only containing internet
        WebElement address = driver.findElement(By.className("current-address"));
        String addressText = address.getText();
        whole.put("address", addressText);

        //list of internet plans
        List<WebElement> planList = driver.findElements(By.className("product-box-container"));
        JSONArray planArray = new JSONArray();

        //get information from each internet plan ------------------------------------------------
        for(WebElement plan : planList) {
            JSONObject planObj = new JSONObject();
            String title = plan.findElement(By.className("internet-pkg-title")).getText();
            planObj.put("title", title);
            //System.out.println(title);
            String description = plan.findElement(By.className("internet-pkg-desc")).getText();
            planObj.put("description", description);
            String dollar = plan.findElement(By.className("price-dollars")).getText();
            String cent = plan.findElement(By.className("price-cents")).getText();
            planObj.put("price", dollar+"."+cent);
            List<WebElement> features = plan.findElements(By.className("speed-tier-feature-copy"));

            //get each feature of the plan, like speed limit
            String[] featuresStr = {"usage limit","download speed","upload speed","ideal for"};
            int i = 0;
            JSONObject featuresObj = new JSONObject();
            for(WebElement feature : features) {
                String f = feature.findElement(By.tagName("strong")).getText();
                featuresObj.put(featuresStr[i], f);
                i++;
                //System.out.println(f);
            }
            planObj.put("features", featuresObj);
            planArray.put(planObj);

        }
        whole.put("internet plan", planArray);  //put internet plan information into json object

        //navigate from internet plans to tv plans--------------------------------------------------------------------
        WebElement shop = driver.findElement(By.xpath("/html/body/div[2]/div/nav/ul[1]/li[1]/a"));
        shop.click();
        WebElement tv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/nav/ul[1]/li[1]/div/div/div[3]/ul/li[1]/div/a")));
        tv.click();
        Thread.sleep(1334);
        WebElement orderTV = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"iptv-pkg-tabgroup-panes\"]/div[2]/div[1]/div[1]/div[2]/div[4]/a[1]")));
        orderTV.click();
        WebElement selectPac = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mat-dialog-0\"]/nx-info-dialog/div[3]/button")));
        Thread.sleep(1124);
        selectPac.click();

        //click phone box
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"teksavvy-order-form\"]/nx-root/nx-order-view/nx-service-selection/div/div/div/div[2]/nx-toggle-button[2]/label")));
        Thread.sleep(1024);
        phone.click();
        Thread.sleep(634);
        WebElement allPlan =driver.findElement(By.xpath("//*[@id=\"sl-tab-2\"]"));
        allPlan.click();


        //list of package plans, combination of internet, phone, and TV---------------------------------------------------------
        List<WebElement> packagePlanList = driver.findElements(By.className("product-box-container"));
        JSONArray PackagePlanArray = new JSONArray();

        //get text from each plan
        for(WebElement plan : packagePlanList) {
            JSONObject planObj = new JSONObject();
            String title = plan.findElement(By.className("product-title")).getText();
            planObj.put("title", title);
            //System.out.println(title);
            String description = plan.findElement(By.className("product-desc")).getText();
            planObj.put("description", description);
            String dollar = plan.findElement(By.className("product-price-lg")).getText();
            String cent = plan.findElement(By.className("product-price-sm")).getText();
            planObj.put("price", dollar+"."+cent);
            List<WebElement> features = plan.findElements(By.xpath(".//ul/li"));

            //get each feature of the plan, like speed limit
            String[] featuresStr = {"usage limit","download speed","upload speed","ideal for"};
            int i = 0;
            JSONObject featuresObj = new JSONObject();
            for(WebElement feature : features) {
                String f = feature.findElement(By.tagName("strong")).getText();
                featuresObj.put(featuresStr[i], f);
                i++;
                //System.out.println(f);
            }
            planObj.put("features", featuresObj);
            PackagePlanArray.put(planObj);

        }
        whole.put("package plan", PackagePlanArray);


        //write data into a json file
        try(FileWriter writer = new FileWriter("src/main/resources/plans/teksavvy.json")){
            writer.write(whole.toString(4));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Thread.sleep(5534);

        driver.quit();
    }


    //-------------------scrape plans for Rogers---------------------------------
    public void RogersCrawler() throws IOException, InterruptedException {
        List<String> Plans = new ArrayList<>(); // This variable is used to store all crawled plans
        String[] plan_formated = new String[5]; // This variable is used to store the extracted information

        WebDriver driver = new ChromeDriver(); // Open browser
        driver.get("https://www.rogers.com/"); // Go to website

        // ========== Part 1: Crawl Internet Plans ==========
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Set explicit wait time limit to 30 seconds

        driver.findElement(By.id("geMainMenuDropdown_1"))
                .findElement(By.cssSelector("div.has-dropdown.w-mx-content"))
                .findElement(By.cssSelector("a[role='button']"))
                .click(); // Find 'Internet' in the top menu and click on it
        Thread.sleep(new Random().nextInt(2000)); // Random wait up to 2 seconds

        driver.findElement(By.xpath("//*[@id=\"geMainMenuDropdown_1\"]/div/div/div/ul/li[1]/ge-link/a")).click(); // 'Shop all internet'
        Thread.sleep(new Random().nextInt(2000));

        // Click 'shop now' or 'check availability'
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.w-100.ds-button.ds-pointer.text-center.mw-100.d-inline-block.-primary.-large.text-no-decoration.ng-star-inserted")));
        driver.findElement(By.cssSelector("a.w-100.ds-button.ds-pointer.text-center.mw-100.d-inline-block.-primary.-large.text-no-decoration.ng-star-inserted")).click();

        // Wait for address input
        wait.until(ExpectedConditions.elementToBeClickable(By.id("ds-form-input-id-0")));
        driver.findElement(By.id("ds-form-input-id-0")).sendKeys("389");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul[role='listbox'] li:nth-child(1)")));
        driver.findElement(By.cssSelector("ul[role='listbox'] li:nth-child(1)")).click();
        driver.findElement(By.id("checkAddressBtn")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.dsa-price.d-inline-flex.flex-column.mw-100.align-items-center")));
        List<WebElement> plans = driver.findElements(By.cssSelector("div.-mb24.col-lg-4.col-md-4.col-sm-6.col-xs-12.ng-star-inserted"));

        for (WebElement plan : plans) {
            WebElement button = plan.findElement(By.cssSelector("div.vertical-tile-component"))
                    .findElement(By.cssSelector("div.ng-star-inserted"))
                    .findElement(By.cssSelector("div.ds-tile.h-100.b-solid.b-1.ds-no-overflow.ds-corners.ds-bg-body-default.ds-shadow.ds-br-body-tile"))
                    .findElement(By.cssSelector("div.dsa-tile-plan.h-100.d-flex.flex-column"))
                    .findElement(By.cssSelector("div.dsa-tile-plan__main.d-flex.flex-column.justify-content-between.flex-grow-1.px-24.pt-16.pb-24"))
                    .findElement(By.cssSelector("div.dsa-tile-plan__ctas.d-flex.flex-column.pt-16.gap-16"))
                    .findElement(By.cssSelector("button.w-100.ds-button.ds-pointer.text-center.mw-100.d-inline-block.-tertiary.-large.text-no-decoration.ng-star-inserted"));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", button);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.tile-see-full-details-modal")));
            Plans.add(driver.findElement(By.cssSelector("div.tile-see-full-details-modal")).getText());

            driver.findElement(By.cssSelector("button[title='Close this window']")).click();
            Thread.sleep(2000);
        }

        // Save to CSV

        String filePath = "src/main/resources/plans/rogers_plans.csv";
        File file = new File(filePath);
        String[] headers = {"name", "dlSpeed", "ulSpeed", "price", "dataLimit"};

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(String.join(",", headers) + "\n");
            }
        }

        for (String plan : Plans) {
            System.out.println("--------------------------------------------------");

            String dlSpeedPattern = "Download speeds up to (\\d+\\s[GM]?bps)\\d*";
            String ulSpeedPattern = "Upload speeds up to (\\d+\\s[GM]?bps)\\d*";
            String pricePattern = "\\$(\\d+(\\.\\d{2})?) per mo*";
            String dataLimitPattern = "(?:Unlimited|(\\d+))\\s*usage";

            String name = (plan.split("\n", 3))[1];
            plan_formated[0] = name;

            plan_formated[1] = extract(plan, dlSpeedPattern);
            plan_formated[2] = extract(plan, ulSpeedPattern);
            plan_formated[3] = extract(plan, pricePattern);
            plan_formated[4] = extract(plan, dataLimitPattern);

            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(String.join(",", plan_formated) + "\n");
                System.out.println("Data written: " + String.join(",", plan_formated));
            }
        }

        driver.quit();
    }

    private static String extract(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1) != null ? m.group(1) : m.group(0); //Return the matched group, handling single and multiple groups
        }
        return "";
    }
}
