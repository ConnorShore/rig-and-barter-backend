package com.rigandbarter.messageservice.componentdbservice.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public abstract class Scraper<T> {

    protected WebDriver webDriver;
    protected String baseUrl;
    protected String outputFilePath;
    protected String baseWindowHandle;

    public Scraper(String url, String outputFilePath) {
        this.baseUrl = url;
        this.outputFilePath = outputFilePath;
        webDriver = new ChromeDriver();
    }

    public void scrapeForObjects() throws IOException {
        webDriver.get(this.baseUrl);
        webDriver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofMillis(1000));
        baseWindowHandle = webDriver.getWindowHandle();

        List<T> ret = scrape();

        webDriver.close();

        ret = cleanAndFilter(ret);
        writeToFile(ret);
    }

    protected abstract List<T> scrape();
    protected abstract List<T> cleanAndFilter(List<T> items);
    protected abstract void writeToFile(List<T> cleanedItems) throws IOException;
}
