package com.rigandbarter.componentscraper.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.componentscraper.model.MemoryComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MemoryScraper extends Scraper<MemoryComponent> {

    public MemoryScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected MemoryComponent retrieveComponentData() {
        return MemoryComponent.builder()
                .category(ComponentCategory.MEMORY)
                .name(findName())
                .imageUrl(findImageUrl())
                .manufacturer(findManufacturer())
                .type(findType())
                .size(findSize())
                .clockSpeed(findClockSpeed())
                .numSticks(findNumSticks())
                .build();
    }

    private String findType() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[1]"));
        return cardBody.getText();
    }

    private int findSize() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[2]"));
        String lengthStr = cardBody.getText()
                .toLowerCase()
                .replace("gb", "")
                .trim();

        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    private int findClockSpeed() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[3]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    private int findNumSticks() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[5]"));
        String lengthStr = cardBody.getText();
        return !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : 0;
    }

    @Override
    protected List<MemoryComponent> cleanAndFilter(List<MemoryComponent> items) {
        return items.stream()
                .map(i -> {
                    String type = i.getType();
                    if(type.indexOf('-') == -1)
                        return i;

                    type = type.substring(0, type.indexOf('-')).trim();
                    i.setType(type);
                    return i;
                })
                .toList();
    }

    @Override
    public String getName() {
        return "Memory Scraper";
    }
}
