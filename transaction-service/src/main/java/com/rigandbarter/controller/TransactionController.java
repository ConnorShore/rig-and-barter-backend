package com.rigandbarter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String base() {
        return "Test endpoint hit";
    }


    @GetMapping(path = "/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        System.out.println("\n\n\n\nTEST ENDPINT HIT\n\n\n\n");
        return "true";
    }

    @GetMapping(path = "/get/{val}")
    @ResponseStatus(HttpStatus.OK)
    public String testVal(@PathVariable(name = "val") int val) {
        return "You requested " + val;
    }
}
