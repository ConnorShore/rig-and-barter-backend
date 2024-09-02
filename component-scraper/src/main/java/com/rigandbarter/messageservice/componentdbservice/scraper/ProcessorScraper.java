package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.ProcessorComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;


public class ProcessorScraper extends Scraper<ProcessorComponent> {

    public ProcessorScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected ProcessorComponent retrieveComponentData() {
        return ProcessorComponent.builder()
                .category(ComponentCategory.CPU)
                .name(findName())
                .imageUrl(findImageUrl())
                .manufacturer(findManufacturer())
                .baseClock(findBaseClock())
                .turboClock(findTurboClock())
                .cores(findCores())
                .threads(findThreads())
                .build();
    }

    @Override
    protected List<ProcessorComponent> cleanAndFilter(List<ProcessorComponent> items) {
        return items;
    }

    @Override
    public String getName() {
        return "Processor Scraper";
    }

    private double findBaseClock() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[1]"));
        String numStrVal = cardBody.getText()
                .toLowerCase()
                .replace("ghz", "")
                .trim();
        return numStrVal.isEmpty() ? 0.0 : Double.parseDouble(numStrVal);
    }

    private double findTurboClock() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[2]"));
        String numStrVal = cardBody.getText()
                .toLowerCase()
                .replace("ghz", "")
                .trim();
        return numStrVal.isEmpty() ? 0.0 : Double.parseDouble(numStrVal);
    }

    private int findCores() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[3]//dd[1]"));
        String numStrVal = cardBody.getText().trim();
        return numStrVal.isEmpty() ? -1 : Integer.parseInt(numStrVal);
    }

    private int findThreads() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[3]//dd[2]"));
        String numStrVal = cardBody.getText().trim();
        return numStrVal.isEmpty() ? -1 : Integer.parseInt(numStrVal);
    }
}
