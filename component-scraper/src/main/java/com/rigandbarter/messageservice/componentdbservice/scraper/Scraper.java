package com.rigandbarter.messageservice.componentdbservice.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
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
        List<T> ret = scrape();
        webDriver.close();

        // Write out failed conversions to error file
        writeOutFailedConversions();

        // Clean the items
        ret = cleanAndFilter(ret);

        // Write out items to output
        writeToFile(ret);
    }

    protected void writeOutFailedConversions() {
        if(failedConversions.isEmpty())
            return;
        // Write failed conversions out if necessary
        try {
            System.out.printf("Failed Conversions for [%s]: %d%n", getName(), failedConversions.size());
            String fileName = outputFilePath.split("\\.")[0] + "-failed.txt";

            FileWriter fileWriter = new FileWriter(fileName);
            for (String str : failedConversions) {
                fileWriter.write(str + System.lineSeparator());
            }

            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Failed to create cases failed file");
        }
    }

    protected abstract List<T> scrape();
    protected abstract List<T> cleanAndFilter(List<T> items);
    protected abstract void writeToFile(List<T> cleanedItems) throws IOException;
    public abstract String getName();
}
