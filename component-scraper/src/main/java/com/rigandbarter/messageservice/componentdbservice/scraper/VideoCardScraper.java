package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.VideoCardComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VideoCardScraper extends Scraper<VideoCardComponent> {

    public VideoCardScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected VideoCardComponent retrieveComponentData() {
        return VideoCardComponent.builder()
                .category(ComponentCategory.GPU)
                .name(findName())
                .imageUrl(findImageUrl())
                .manufacturer(findManufacturer())
                .length(findLength())
                .slots(findSlots())
                .numHDMIs(findNumHDMIs())
                .numDisplayPorts(findNumDisplayPorts())
                .boostClock(findBoostClock())
                .vram(findVRAM())
                .build();
    }

    @Override
    protected List<VideoCardComponent> cleanAndFilter(List<VideoCardComponent> items) {
        return items;
    }

    @Override
    public String getName() {
        return "Video Card Scraper";
    }
    private double findLength() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[1]"));
        String lengthStr = cardBody.getText()
                .toLowerCase()
                .replace("mm", "")
                .trim();

        return !lengthStr.isEmpty() ? Double.parseDouble(lengthStr) : 0;
    }

    private double findSlots() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[2]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Double.parseDouble(lengthStr) : 0;
    }

    private int findNumHDMIs() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[3]//dd[1]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    private int findNumDisplayPorts() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[3]//dd[2]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    private int findBoostClock() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[4]//dd[1]"));
        String lengthStr = cardBody.getText()
                .toLowerCase()
                .replace("mhz", "")
                .trim();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    private int findVRAM() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[4]//dd[2]"));
        String lengthStr = cardBody.getText()
                .toLowerCase()
                .replace("mb", "")
                .replace("gb", "")
                .trim();

        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }
}
