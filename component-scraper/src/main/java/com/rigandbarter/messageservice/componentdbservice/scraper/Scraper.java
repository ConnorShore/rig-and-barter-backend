package com.rigandbarter.messageservice.componentdbservice.scraper;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    /**
     * Methods to implement by different scrapers
     */
    protected abstract List<T> scrape();
    protected abstract List<T> cleanAndFilter(List<T> items);
    public abstract String getName();


    /**
     * Writes out failed conversion urls to text file
     */
    private void writeOutFailedConversions() {
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
}
