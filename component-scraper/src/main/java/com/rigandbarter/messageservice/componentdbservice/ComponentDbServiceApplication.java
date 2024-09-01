package com.rigandbarter.messageservice.componentdbservice;

import com.rigandbarter.messageservice.componentdbservice.scraper.CaseScraper;
import com.rigandbarter.messageservice.componentdbservice.scraper.MotherboardScraper;
import com.rigandbarter.messageservice.componentdbservice.scraper.ProcessorScraper;
import com.rigandbarter.messageservice.componentdbservice.scraper.Scraper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ComponentDbServiceApplication {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static final String OUTPUT_ZIP_NAME = "data_files.zip";

    public static void main(String[] args) {
        CaseScraper caseScraper = new CaseScraper("https://www.pc-kombo.com/us/components/cases", "cases.csv");
        MotherboardScraper motherboardScraper = new MotherboardScraper("https://www.pc-kombo.com/us/components/motherboards", "motherboards.csv");
        ProcessorScraper processorScraper = new ProcessorScraper("https://www.pc-kombo.com/us/components/cpus", "processors.csv");

        executorService.execute(createScraperRunnable(caseScraper));
        executorService.execute(createScraperRunnable(motherboardScraper));
        executorService.execute(createScraperRunnable(processorScraper));

        executorService.shutdown();

        while (!executorService.isTerminated()) { }

        String[] fileNamesToAdd = {"cases.csv", "motherboards.csv", "processors.csv"};
        createZipAndAddFiles(fileNamesToAdd);

        System.out.println("All scrapers have finished execution");
        System.exit(0);
    }

    private static void createZipAndAddFiles(String... fileNamesToAdd) {
        try {
            FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_NAME);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String aFile : fileNamesToAdd) {
                zos.putNextEntry(new ZipEntry(new File(aFile).getName()));

                byte[] bytes = Files.readAllBytes(Paths.get(aFile));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();

                Files.delete(Path.of(aFile));
            }

            zos.close();

            System.out.println("Data files zipped successfully");

        } catch (FileNotFoundException ex) {
            System.err.println("A file does not exist: " + ex);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
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