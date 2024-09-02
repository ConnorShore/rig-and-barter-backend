package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.HardDriveComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HardDriveScraper extends Scraper<HardDriveComponent> {
    public HardDriveScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected HardDriveComponent retrieveComponentData() {
        return HardDriveComponent.builder()
                .category(ComponentCategory.HARD_DRIVE)
                .name(findName())
                .imageUrl(findImageUrl())
                .manufacturer(findManufacturer())
                .size(findSize())
                .rpm(findRPM())
                .cacheSize(findCacheSize())
                .build();
    }

    private int findSize() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[2]"));
        String intStr = cardBody.getText().toLowerCase();
        boolean inTB = intStr.contains("tb");
        intStr = intStr
                .replace("tb", "")
                .replace("gb", "")
                .trim();
        int size = !intStr.isEmpty() ? Integer.parseInt(intStr) : 0;
        return inTB ? size * 100 : size;
    }

    private double findRPM() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[3]"));
        String doubleStr = cardBody.getText().toLowerCase();
        return !doubleStr.isEmpty() ? Double.parseDouble(doubleStr) : 0;
    }

    private int findCacheSize() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[4]"));
        String intStr = cardBody.getText();
        return !intStr.isEmpty() ? Integer.parseInt(intStr) : 0;
    }

    @Override
    protected List<HardDriveComponent> cleanAndFilter(List<HardDriveComponent> items) {
        return items;
    }

    @Override
    public String getName() {
        return "Hard Drive Scraper";
    }
}
