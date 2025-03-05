package com.rigandbarter.componentscraper;

import com.rigandbarter.componentscraper.scraper.*;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ComponentScraperApplication {
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

    // TODO: Move to config
    private static final String POST_URL = "https://pc-rig-and-barter.com/api/component/db";
//    private static final String POST_URL = "http://localhost:9000/api/component/db";

    public static void main(String[] args) {
        try {
            // TODO: Have cmd kargs to allow to just post already created zip files
            //  so re-scraping doesn't need to occur.

//            scrapeContent();
//            File zipFile = packageScrapedContent();
            File zipFile = new File("./component-scraper/dist/out/data_files.zip");
            sendPackagedContent(zipFile);

            System.out.println("All scrapers have finished execution!");
            System.exit(0);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Send the content to the component-service to be saved
     */
    private static void sendPackagedContent(File file) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(POST_URL);
        ContentBody fileContent = new FileBody(file); //For tar.gz: "application/x-gzip"
        HttpEntity multipart = MultipartEntityBuilder.create()
                .addPart("dataZip", fileContent)
                .build();

        httpPost.setEntity(multipart);
        HttpResponse response = httpClient.execute(httpPost);
        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED)
            throw new RuntimeException("Failed to post contents to component-service");
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
    private static File packageScrapedContent() throws IOException {
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
        File outFile = createZipAndAddFiles(outDir, OUTPUT_ZIP_NAME, DATA_FILE_NAMES);
        createZipAndAddFiles(outDir, FAILED_ZIP_NAME, FAILED_FILE_NAMES);
        return outFile;
    }

    /**
     * Creates a zip file that contains the list of all files
     * @param directory The directory of the zip file
     * @param fileName The name of the zip file
     * @param fileNamesToAdd The list of files to add to the zip
     */
    private static File createZipAndAddFiles(File directory, String fileName, String... fileNamesToAdd) {
        ZipOutputStream zos;
        try {
            FileOutputStream fos = new FileOutputStream(String.valueOf(Paths.get(directory.getAbsolutePath(), fileName)));
            zos = new ZipOutputStream(fos);

            for (String aFile : fileNamesToAdd) {
                zos.putNextEntry(new ZipEntry(new File(aFile).getName()));

                byte[] bytes = Files.readAllBytes(Paths.get(aFile));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();

                Files.delete(Path.of(aFile));
            }

            zos.close();
            System.out.printf("Data files zipped successfully [%s]\n", fileName);

            return new File(String.valueOf(Paths.get(directory.getAbsolutePath(), fileName)));

        } catch (FileNotFoundException ex) {
            System.err.println("A file does not exist: " + ex);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }

        return null;
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