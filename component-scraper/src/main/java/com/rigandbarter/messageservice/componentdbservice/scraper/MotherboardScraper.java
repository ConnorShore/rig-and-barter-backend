package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.CaseComponent;
import com.rigandbarter.messageservice.componentdbservice.model.MotherboardComponent;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MotherboardScraper extends Scraper<MotherboardComponent> {


    public MotherboardScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected List<MotherboardComponent> scrape() {
        List<WebElement> urls = webDriver.findElements(By.cssSelector("li.columns a"));

        List<MotherboardComponent> motherboardComponents = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for(WebElement url : urls) {
            String urlStr = url.getAttribute("href");
            if(visited.contains(urlStr))
                continue;

            MotherboardComponent c = retrieveMotherboardData(urlStr);

            if(c != null)
                motherboardComponents.add(c);

            visited.add(urlStr);
        }

        return motherboardComponents;
    }

    @Override
    protected List<MotherboardComponent> cleanAndFilter(List<MotherboardComponent> items) {
        return items;
    }

    @Override
    public String getName() {
        return "Motherboard Scraper";
    }

    private MotherboardComponent retrieveMotherboardData(String url) {
        if(!url.contains("pc-kombo"))
            return null;

        MotherboardComponent motherboardComponent = null;
        boolean tabOpen = false;
        try {
            webDriver.switchTo().newWindow(WindowType.TAB);
            tabOpen = true;

            webDriver.get(url);
            webDriver.manage()
                    .timeouts()
                    .implicitlyWait(Duration.ofMillis(1000));

            motherboardComponent = MotherboardComponent.builder()
                    .category(ComponentCategory.MOTHERBOARD)
                    .name(findName())
                    .imageUrl(findImageUrl())
                    .manufacturer(findManufacturer())
                    .type(findType())
                    .socket(findSocket())
                    .memoryType(findMemoryType())
                    .memoryCapacity(findMemoryCapacity())
                    .memorySlots(findMemorySlots())
                    .build();

//            caseComponent = CaseComponent.builder()
//                    .category(ComponentCategory.CASE)
//                    .name(findName())
//                    .imageUrl(findImageUrl())
//                    .manufacturer(findManufacturer())
//                    .motherboardType(findMotherboardType())
//                    .powerSupplyType(findPowerSupplyType())
//                    .gpuLength(findGpuLength())
//                    .color(null)        // will be acquired in clean phase
//                    .windowed(false)    // will be acquired in the clean phase
//                    .build();
        } catch (Exception e) {
            failedConversions.add(url);
        }
        finally {
            if(tabOpen)
                webDriver.close();

            webDriver.switchTo().window(baseWindowHandle);
        }

        return motherboardComponent;
    }

    private String findName() {
        return webDriver.findElement(By.tagName("h1")).getText();
    }

    private String findImageUrl() {
        WebElement imgElem = webDriver.findElement(By.cssSelector("img.img-responsive"));
        if(imgElem == null)
            return "";

        return imgElem.getAttribute("src");
    }

    private String findManufacturer() {
        return webDriver.findElement(By.cssSelector("dd[itemprop=\"brand\"]")).getText();
    }

    private String findType() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[4]"));
        return cardBody.getText();
    }

    private String findSocket() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[1]"));
        return cardBody.getText();
    }

    private String findMemoryType() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[3]//dd[1]"));
        return cardBody.getText().trim();
    }

    private int findMemoryCapacity() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[3]//dd[2]"));
        String numStrVal = cardBody.getText().trim();
        return numStrVal.isEmpty() ? -1 : Integer.parseInt(numStrVal);
    }

    private int findMemorySlots() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[3]//dd[3]"));
        String numStrVal = cardBody.getText().trim();
        return numStrVal.isEmpty() ? -1 : Integer.parseInt(numStrVal);
    }
}
