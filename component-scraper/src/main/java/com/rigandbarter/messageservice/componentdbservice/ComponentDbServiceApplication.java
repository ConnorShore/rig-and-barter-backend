package com.rigandbarter.messageservice.componentdbservice;

import com.rigandbarter.messageservice.componentdbservice.scraper.CaseScraper;

import java.io.IOException;

public class ComponentDbServiceApplication {
    public static void main(String[] args) throws IOException {
        CaseScraper caseScraper = new CaseScraper("https://www.pc-kombo.com/us/components/cases", "cases.csv");
        caseScraper.scrapeForObjects();
    }
}