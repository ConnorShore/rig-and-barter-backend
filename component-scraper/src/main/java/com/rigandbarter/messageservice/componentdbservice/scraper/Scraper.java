package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Scraper<T> {
    protected WebDriver webDriver;
    protected String baseUrl;
    protected String outputFilePath;
    protected String baseWindowHandle;

    protected final Set<String> failedConversions;

    public Scraper(String url, String outputFilePath) {
        this.baseUrl = url;
        this.outputFilePath = outputFilePath;
        this.failedConversions = new HashSet<>();

        webDriver = new ChromeDriver();
    }

    public void scrapeForObjects() throws IOException {
        // Setup webDriver
        webDriver.get(this.baseUrl);
        webDriver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofMillis(1000));

        // Scrape out objects
        baseWindowHandle = webDriver.getWindowHandle();
        List<T> scrapedComponents = scrape();
        webDriver.close();

        // Write out failed conversions to error file
        writeOutFailedConversions();

        // Clean the items
        scrapedComponents = cleanAndFilter(scrapedComponents);

        // Write out items to output
        writeToFile(scrapedComponents);
    }

    /**
     * Methods to implement by different scrapers
     */
    protected abstract T retrieveComponentData();
    protected abstract List<T> cleanAndFilter(List<T> items);
    public abstract String getName();

    private List<T> scrape() {
        List<WebElement> urls = webDriver.findElements(By.cssSelector("li.columns a"));

        List<T> components = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for(WebElement url : urls) {
            String urlStr = url.getAttribute("href");
            if(visited.contains(urlStr))
                continue;

            T c = scrapeComponent(urlStr);

            if(c != null)
                components.add(c);

            visited.add(urlStr);
        }

        return components;
    }

    private T scrapeComponent(String url) {
        if(!url.contains("pc-kombo"))
            return null;

        T component = null;
        boolean tabOpen = false;
        try {
            webDriver.switchTo().newWindow(WindowType.TAB);
            tabOpen = true;

            webDriver.get(url);
            webDriver.manage()
                    .timeouts()
                    .implicitlyWait(Duration.ofMillis(1000));

            component = retrieveComponentData();

        } catch (Exception e) {
            failedConversions.add(url);
        }
        finally {
            if(tabOpen)
                webDriver.close();

            webDriver.switchTo().window(baseWindowHandle);
        }

        return component;
    }

    /**
     * Writes out failed conversion urls to text file
     */
    private void writeOutFailedConversions() {
        // Write failed conversions
        try {
            System.out.printf("Failed Conversions for [%s]: %d%n", getName(), failedConversions.size());
            String fileName = outputFilePath.split("\\.")[0] + "-failed.txt";

            FileWriter fileWriter = new FileWriter(fileName);
            for (String str : failedConversions) {
                fileWriter.write(str + System.lineSeparator());
            }

            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Failed to create cases failed file for: " + outputFilePath);
        }
    }

    /**\
     * Writes out the list of components to a csv file
     * @param cleanedItems The cleaned items to write out
     */
    private void writeToFile(List<T> cleanedItems) {
        Path outputPath = Path.of(outputFilePath);
        try (var writer = Files.newBufferedWriter(outputPath)) {
            StatefulBeanToCsv<T> csv = new StatefulBeanToCsvBuilder<T>(writer)
                    .build();
            csv.write(cleanedItems);
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String findName() {
        return webDriver.findElement(By.tagName("h1")).getText();
    }

    protected String findImageUrl() {
        try {
            WebElement imgElem = webDriver.findElement(By.cssSelector("img.img-responsive"));
            if(imgElem == null)
                return "";

            return imgElem.getAttribute("src");
        } catch (Exception e) {
            return "";
        }
    }

    protected String findManufacturer() {
        return webDriver.findElement(By.cssSelector("dd[itemprop=\"brand\"]")).getText();
    }
}
