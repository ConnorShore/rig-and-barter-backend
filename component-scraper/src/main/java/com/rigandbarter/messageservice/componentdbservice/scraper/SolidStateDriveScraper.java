package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.SolidStateDriveComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SolidStateDriveScraper extends Scraper<SolidStateDriveComponent> {
    public SolidStateDriveScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected SolidStateDriveComponent retrieveComponentData() {
        return SolidStateDriveComponent.builder()
                .category(ComponentCategory.SOLID_STATE_DRIVE)
                .name(findName())
                .imageUrl(findImageUrl())
                .manufacturer(findManufacturer())
                .formFactor(findFormFactor())
                .protocol(findProtocol())
                .size(findSize())
                .build();
    }

    private String findFormFactor() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[1]"));
        return cardBody.getText();
    }

    private String findProtocol() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[2]"));
        return cardBody.getText();
    }

    private int findSize() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[3]"));
        String intStr = cardBody.getText().toLowerCase();
        boolean inTB = intStr.contains("tb");
        intStr = intStr
                .replace("tb", "")
                .replace("gb", "")
                .trim();
        int size = !intStr.isEmpty() ? Integer.parseInt(intStr) : 0;
        return inTB ? size * 100 : size;
    }

    @Override
    protected List<SolidStateDriveComponent> cleanAndFilter(List<SolidStateDriveComponent> items) {
        return items;
    }

    @Override
    public String getName() {
        return "Solid State Drive Scraper";
    }
}
