package com.dataset.automation.controller;

import com.dataset.automation.service.AutomationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/automation")
@Slf4j
public class AutomationController {

    @Autowired
    private final AutomationService service;

    public AutomationController(AutomationService service) {
        this.service = service;
    }

    @PostMapping("/createDataset/{num}")
    public ResponseEntity<String> createDataset(@PathVariable("num") int num){
        try{
            service.createDataset(num);
            return ResponseEntity.ok("Dataset Creation Process Finished.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encountered while creating Dataset.");
        }
    }

    @GetMapping("/testing")
    public String testing(){
        log.info("Test Endpoint Reached.");
        return "Test Endpoint Reached.";
    }

    @GetMapping("download/dataset")
    public ResponseEntity downloadDataset(HttpServletResponse response){
        try{
            log.info("Starting download.........");
            service.exportToCsv(response);
            log.info("Download Finished.........");
            return ResponseEntity.status(HttpStatus.OK).body("Download Complete");
        } catch (IOException e) {
            log.error("Error occurred while downloading CSV File.");
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Download Failed Due to Internal Server Error.");
        }
    }
}
