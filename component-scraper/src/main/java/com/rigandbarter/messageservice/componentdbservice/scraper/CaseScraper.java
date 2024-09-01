package com.rigandbarter.messageservice.componentdbservice.scraper;
import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.messageservice.componentdbservice.model.CaseComponent;
import org.apache.commons.text.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

import java.time.Duration;
import java.util.*;
import java.util.List;

public class CaseScraper extends Scraper<CaseComponent> {

    private final String[] OMITTED_STRINGS = {"windowed", "window", "tempered", "isolated", "glass", "edition", "drgb", "argb"};

    public CaseScraper(String url, String outputFilePath) {
        super(url, outputFilePath);
    }

    @Override
    protected CaseComponent retrieveComponentData(String url) {
        if(!url.contains("pc-kombo"))
            return null;

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

//                        if(color.isEmpty())
//                            System.out.println("Null color for: " + name);

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
    public String getName() {
        return "Case Scraper";
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
