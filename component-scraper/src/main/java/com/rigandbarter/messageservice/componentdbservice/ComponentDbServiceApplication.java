package com.rigandbarter.messageservice.componentdbservice;

import com.rigandbarter.messageservice.componentdbservice.scraper.CaseScraper;
import com.rigandbarter.messageservice.componentdbservice.scraper.Scraper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComponentDbServiceApplication {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        CaseScraper caseScraper = new CaseScraper("https://www.pc-kombo.com/us/components/cases", "cases.csv");
        executorService.execute(createScraperRunnable(caseScraper));

        executorService.shutdown();

        while (!executorService.isTerminated()) { }

        System.out.println("All scrapers have finished execution");
        System.exit(0);
    }

    private static <T> Runnable createScraperRunnable(Scraper<T> scraper) {
        return () -> {
            try {
                scraper.scrapeForObjects();
            } catch (IOException e) {
                System.err.println("Scraper failed: " + scraper.getName());
                throw new RuntimeException(e);
            }
        };
    }
}