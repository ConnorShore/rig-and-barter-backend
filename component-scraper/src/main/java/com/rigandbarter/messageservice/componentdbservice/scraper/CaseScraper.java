package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.CaseComponent;
import org.apache.commons.text.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class CaseScraper extends Scraper<CaseComponent> {

    private final String[] OMITTED_STRINGS = {"windowed", "window", "tempered", "isolated", "glass", "edition", "drgb", "argb"};

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

        return caseComponents;
    }

    @Override
    protected List<CaseComponent> cleanAndFilter(List<CaseComponent> items) {
        return items.stream()
                .filter(i -> i.getName() != null)
                .peek(item -> {
                    int stage = 0;
                    String name = item.getName();
                    try {
                        int lastHyphen = name.lastIndexOf(" -- ");
                        int index = 4;
                        if(lastHyphen == -1) {
                            lastHyphen = name.lastIndexOf(" - ");
                            index = 3;
                        }
                        stage = 1;

                        String colorWindowStr = "";
                        String updatedName = name;
                        if(lastHyphen != -1) {
                            colorWindowStr = name.substring(lastHyphen+index).toLowerCase();
                            updatedName = name.substring(0, lastHyphen).trim();
                        }

                        stage = 2;

                        // See if it has window
                        boolean hasWindow = colorWindowStr.contains("window") || colorWindowStr.contains("glass");

                        // Remove strings to get color alone
                        for(String str : OMITTED_STRINGS)
                            colorWindowStr = colorWindowStr.replaceAll(str, "");

                        stage = 3;

                        // Get the color
                        String color = colorWindowStr.trim();;
                        if(color.contains("/")) {
                            // Get first color if there are multiple colors
                            color = color.split("/")[0];
                        }

                        color = color.replaceAll("[^a-zA-Z ]", "").trim();

                        if(color.isEmpty())
                            System.out.println("Null color for: " + name);

                        stage = 4;

                        // Use updated values
                        item.setName(updatedName);
                        item.setWindowed(hasWindow);
                        item.setColor(WordUtils.capitalizeFully(color));
                    } catch (Exception e) {
                        System.err.printf("Failed to convert item: %s | Stage = %d\n", name, stage);
                    }
                })
                .toList();
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

    @Override
    public String getName() {
        return "Case Scraper";
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
        WebElement imgElem = webDriver.findElement(By.cssSelector("img.img-responsive"));
        if(imgElem == null)
            return "";

        return imgElem.getAttribute("src");
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
