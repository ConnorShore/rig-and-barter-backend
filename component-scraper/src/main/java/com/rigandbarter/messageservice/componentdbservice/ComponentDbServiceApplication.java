package com.rigandbarter.messageservice.componentdbservice;

import com.rigandbarter.messageservice.componentdbservice.scraper.*;
import org.apache.commons.io.FileUtils;

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
    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static final String OUTPUT_ZIP_NAME = "data_files.zip";
    private static final String FAILED_ZIP_NAME = "failed_files.zip";
    private static final String OUTPUT_DIRECTORY = "/component-scraper/dist/out";
    private static final String RECENT_RUNS_DIRECTORY = "/component-scraper/dist/recent";
    private static final String[] DATA_FILE_NAMES = {
            "case.csv", "motherboard.csv", "cpu.csv",
            "gpu.csv", "memory.csv", "power-supply.csv",
            "hard-drive.csv", "solid-state-drive.csv"
    };
    private static final String[] FAILED_FILE_NAMES = {
            "case-failed.txt", "motherboard-failed.txt", "cpu-failed.txt",
            "gpu-failed.txt", "memory-failed.txt", "power-supply-failed.txt",
            "hard-drive-failed.txt", "solid-state-drive-failed.txt"
    };

    public static void main(String[] args) throws IOException {
        scrapeContent();
        packageScrapedContent();

        System.out.println("All scrapers have finished execution!");
        System.exit(0);
    }

    /**
     * Scrapes content for all component types
     */
    private static void scrapeContent() {
        CaseScraper caseScraper = new CaseScraper("https://www.pc-kombo.com/us/components/cases", "case.csv");
        MotherboardScraper motherboardScraper = new MotherboardScraper("https://www.pc-kombo.com/us/components/motherboards", "motherboard.csv");
        ProcessorScraper processorScraper = new ProcessorScraper("https://www.pc-kombo.com/us/components/cpus", "cpu.csv");
        VideoCardScraper videoCardScraper = new VideoCardScraper("https://www.pc-kombo.com/us/components/gpus", "gpu.csv");
        MemoryScraper memoryScraper = new MemoryScraper("https://www.pc-kombo.com/us/components/rams", "memory.csv");
        PowerSupplyScraper powerSupplyScraper = new PowerSupplyScraper("https://www.pc-kombo.com/us/components/psus", "power-supply.csv");
        HardDriveScraper hardDriveScraper = new HardDriveScraper("https://www.pc-kombo.com/us/components/hdds", "hard-drive.csv");
        SolidStateDriveScraper solidStateDriveScraper = new SolidStateDriveScraper("https://www.pc-kombo.com/us/components/ssds", "solid-state-drive.csv");;

        executorService.execute(createScraperRunnable(caseScraper));
        executorService.execute(createScraperRunnable(motherboardScraper));
        executorService.execute(createScraperRunnable(processorScraper));
        executorService.execute(createScraperRunnable(videoCardScraper));
        executorService.execute(createScraperRunnable(memoryScraper));
        executorService.execute(createScraperRunnable(powerSupplyScraper));
        executorService.execute(createScraperRunnable(hardDriveScraper));
        executorService.execute(createScraperRunnable(solidStateDriveScraper));

        executorService.shutdown();

        while (!executorService.isTerminated()) { }
    }

    /**
     * Package the zipped data files
     * @throws IOException Fails on io
     */
    private static void packageScrapedContent() throws IOException {
        // Store previous output files to the recents directory to do diffs on later
        String out = System.getProperty("user.dir") + OUTPUT_DIRECTORY;
        File outDir = new File(out);

        if(outDir.exists()) {
            String recentRuns = System.getProperty("user.dir") + RECENT_RUNS_DIRECTORY;
            File recentRunsDir = new File(recentRuns);

            FileUtils.deleteDirectory(recentRunsDir);
            recentRunsDir.mkdirs();

            FileUtils.copyDirectory(outDir, recentRunsDir);
        } else {
            outDir.mkdirs();
        }

        // Create and zip up data files and failed files
        createZipAndAddFiles(outDir, OUTPUT_ZIP_NAME, DATA_FILE_NAMES);
        createZipAndAddFiles(outDir, FAILED_ZIP_NAME, FAILED_FILE_NAMES);
    }

    /**
     * Creates a zip file that contains the list of all files
     * @param directory The directory of the zip file
     * @param fileName The name of the zip file
     * @param fileNamesToAdd The list of files to add to the zip
     */
    private static void createZipAndAddFiles(File directory, String fileName, String... fileNamesToAdd) {
        try {
            FileOutputStream fos = new FileOutputStream(String.valueOf(Paths.get(directory.getAbsolutePath(), fileName)));
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

    /**
     * Creates a runnable object for the passed in scraper
     * @param scraper The scraper to create a runnable for
     * @return The runnable version of the scaper
     * @param <T> The component type for the scraper
     */
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