package com.rigandbarter.messageservice.componentdbservice;

import com.rigandbarter.messageservice.componentdbservice.scraper.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ComponentDbServiceApplication {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(6);
    private static final String OUTPUT_ZIP_NAME = "data_files.zip";
    private static final String FAILED_ZIP_NAME = "failed_files.zip";

    public static void main(String[] args) {
        CaseScraper caseScraper = new CaseScraper("https://www.pc-kombo.com/us/components/cases", "case.csv");
        MotherboardScraper motherboardScraper = new MotherboardScraper("https://www.pc-kombo.com/us/components/motherboards", "motherboard.csv");
        ProcessorScraper processorScraper = new ProcessorScraper("https://www.pc-kombo.com/us/components/cpus", "cpu.csv");
        VideoCardScraper videoCardScraper = new VideoCardScraper("https://www.pc-kombo.com/us/components/gpus", "gpu.csv");
        MemoryScraper memoryScraper = new MemoryScraper("https://www.pc-kombo.com/us/components/rams", "memory.csv");
        PowerSupplyScraper powerSupplyScraper = new PowerSupplyScraper("https://www.pc-kombo.com/us/components/psus", "power-supply.csv");

        executorService.execute(createScraperRunnable(caseScraper));
        executorService.execute(createScraperRunnable(motherboardScraper));
        executorService.execute(createScraperRunnable(processorScraper));
        executorService.execute(createScraperRunnable(videoCardScraper));
        executorService.execute(createScraperRunnable(memoryScraper));
        executorService.execute(createScraperRunnable(powerSupplyScraper));

        executorService.shutdown();

        while (!executorService.isTerminated()) { }

        // Write out data files and failed files
        String[] fileNamesToAdd = {
                "case.csv", "motherboard.csv", "cpu.csv",
                "gpu.csv", "memory.csv", "power-supply.csv"
        };
        createZipAndAddFiles(OUTPUT_ZIP_NAME, fileNamesToAdd);

        String[] failedNamesToAdd = {
                "case-failed.txt", "motherboard-failed.txt", "cpu-failed.txt",
                "gpu-failed.txt", "memory-failed.txt", "power-supply-failed.txt"
        };
        createZipAndAddFiles(FAILED_ZIP_NAME, failedNamesToAdd);

        System.out.println("All scrapers have finished execution!");
        System.exit(0);
    }

    private static void createZipAndAddFiles(String fileName, String... fileNamesToAdd) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String aFile : fileNamesToAdd) {
                zos.putNextEntry(new ZipEntry(new File(aFile).getName()));

                byte[] bytes = Files.readAllBytes(Paths.get(aFile));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();

                Files.delete(Path.of(aFile));
            }

            zos.close();

            System.out.printf("Data files zipped successfully [%s]\n", fileName);

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