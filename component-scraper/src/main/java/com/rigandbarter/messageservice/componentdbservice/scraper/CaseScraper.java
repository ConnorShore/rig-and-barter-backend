package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.CaseComponent;
import org.apache.commons.collections.ArrayStack;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

public class CaseScraper extends Scraper<CaseComponent> {

    private final Set<String> failedConversions = new HashSet<>();

    public CaseScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected List<CaseComponent> scrape() {
        List<WebElement> urls = webDriver.findElements(By.cssSelector("li.columns a"));

        List<CaseComponent> caseComponents = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        String urlStr = null;
        for(WebElement url : urls) {
            try {
                urlStr = url.getAttribute("href");
                if(visited.contains(urlStr))
                    continue;

                CaseComponent c = retrieveCaseData(urlStr);

                if(c != null)
                    caseComponents.add(c);

                visited.add(urlStr);
            } catch (Exception e) {
                failedConversions.add(urlStr);
                System.err.println(e.getMessage());
            }
        }

        System.out.println("FAILED CONVERSIONS: " + failedConversions.size());
        return caseComponents;
    }

    @Override
    protected List<CaseComponent> cleanAndFilter(List<CaseComponent> items) {
        return items;
    }

    @Override
    protected void writeToFile(List<CaseComponent> cleanedItems) throws IOException {
        Path outputPath = Path.of(outputFilePath);
        try (var writer = Files.newBufferedWriter(outputPath)) {
            StatefulBeanToCsv<CaseComponent> csv = new StatefulBeanToCsvBuilder<CaseComponent>(writer)
                    .build();
            csv.write(cleanedItems);
        } catch (CsvRequiredFieldEmptyException e) {
            throw new RuntimeException(e);
        } catch (CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }
    }

    private CaseComponent retrieveCaseData(String url) {
        if(!url.contains("pc-kombo"))
            return null;

//        String prevWindowHandle = webDriver.getWindowHandle();
        CaseComponent caseComponent = null;
        boolean tabOpen = false;
        try {
            webDriver.switchTo().newWindow(WindowType.TAB);
            tabOpen = true;

            webDriver.get(url);
            webDriver.manage()
                    .timeouts()
                    .implicitlyWait(Duration.ofMillis(1000));

            caseComponent = CaseComponent.builder()
                    .category(ComponentCategory.CASE)
                    .name(findName())
                    .imageUrl(findImageUrl())
                    .manufacturer(findManufacturer())
                    .motherboardType(findMotherboardType())
                    .powerSupplyType(findPowerSupplyType())
                    .gpuLength(findGpuLength())
                    .color(null)        // will be acquired in clean phase
                    .windowed(false)    // will be acquired in the clean phase
                    .build();
        } catch (Exception e) {
            failedConversions.add(url);
        }
        finally {
            if(tabOpen)
                webDriver.close();

            webDriver.switchTo().window(baseWindowHandle);
        }

        return caseComponent;
    }

    private String findName() {
        return webDriver.findElement(By.tagName("h1")).getText();
    }

    private String findImageUrl() {
        return webDriver.findElement(By.cssSelector("img.img-responsive"))
                .getAttribute("src");
    }

    private String findManufacturer() {
        return webDriver.findElement(By.cssSelector("dd[itemprop=\"brand\"]")).getText();
    }

    private String findMotherboardType() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[4]"));
        return cardBody.getText();
    }

    private String findPowerSupplyType() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[5]"));
        return cardBody.getText();
    }

    private int findGpuLength() {
        WebElement specDiv = webDriver.findElement(By.id("specs"));
        WebElement section = specDiv.findElement(By.xpath("//section[2]"));
        WebElement cardBody = section.findElement(By.xpath("(//div[contains(@class, 'card-body')])[2]//dd[6]"));
        return Integer.parseInt(cardBody.getText().replace("mm","").trim());
    }
}
