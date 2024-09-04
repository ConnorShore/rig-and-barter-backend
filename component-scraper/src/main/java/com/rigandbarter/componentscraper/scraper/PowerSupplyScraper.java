package com.rigandbarter.componentscraper.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.componentscraper.model.PowerSupplyComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PowerSupplyScraper extends Scraper<PowerSupplyComponent> {
    public PowerSupplyScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected PowerSupplyComponent retrieveComponentData() {
        return PowerSupplyComponent.builder()
                .category(ComponentCategory.POWER_SUPPLY)
                .name(findName())
                .imageUrl(findImageUrl())
                .manufacturer(findManufacturer())
                .connector(findConnector())
                .watts(findWatts())
                .num6PinPCIE(findNum6PinPCIE())
                .num8PinPCIE(findNum8PinPCIE())
                .build();
    }

    private String findConnector() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[2]"));
        return cardBody.getText();
    }

    private int findWatts() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[1]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    private int findNum6PinPCIE() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[5]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    private int findNum8PinPCIE() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[4]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    @Override
    protected List<PowerSupplyComponent> cleanAndFilter(List<PowerSupplyComponent> items) {
        return items;
    }

    @Override
    public String getName() {
        return "Power Supply Scraper";
    }
}
