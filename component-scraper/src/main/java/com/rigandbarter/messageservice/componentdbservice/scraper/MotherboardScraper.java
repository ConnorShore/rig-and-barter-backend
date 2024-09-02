package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.MotherboardComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

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
    protected MotherboardComponent retrieveComponentData() {
        return MotherboardComponent.builder()
                .category(ComponentCategory.MOTHERBOARD)
                .name(findName())
                .imageUrl(findImageUrl())
                .manufacturer(findManufacturer())
                .connector(findConnector())
                .socket(findSocket())
                .memoryType(findMemoryType())
                .memoryCapacity(findMemoryCapacity())
                .memorySlots(findMemorySlots())
                .build();
    }

    private String findConnector() {
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
