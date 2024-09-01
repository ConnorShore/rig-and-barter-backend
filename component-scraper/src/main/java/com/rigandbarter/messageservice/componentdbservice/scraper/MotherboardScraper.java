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
    protected List<MotherboardComponent> cleanAndFilter(List<MotherboardComponent> items) {
        return items;
    }

    @Override
    public String getName() {
        return "Motherboard Scraper";
    }

    @Override
    protected MotherboardComponent retrieveComponentData(String url) {
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
