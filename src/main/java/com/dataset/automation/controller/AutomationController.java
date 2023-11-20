package com.dataset.automation.controller;

import com.dataset.automation.dao.DatasetRepo;
import com.dataset.automation.model.DatasetObject;
import com.dataset.automation.service.AutomationService;
import com.dataset.automation.service.CrudService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
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

    @Autowired
    private final CrudService databaseService;

    @Autowired
    private final DatasetRepo repo;


    public AutomationController(AutomationService service, CrudService databaseService, DatasetRepo repo) {
        this.service = service;
        this.databaseService = databaseService;
        this.repo = repo;
    }

    @PostMapping("/createDataset/{num}")
    public ResponseEntity<String> createDataset(@PathVariable("num") int num) {
        try {
            service.createDataset(num);
            return ResponseEntity.ok("Dataset Creation Process Finished.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encountered while creating Dataset.");
        }
    }

    @GetMapping("/testing")
    public String testing() {
        log.info("Test Endpoint Reached.");
        return "Test Endpoint Reached.";
    }

    @GetMapping("download/dataset")
    public void downloadDataset(HttpServletResponse response) {
        try {
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
            log.info("Starting download.........");
            service.exportToCsv(responseWrapper);
            log.info("Download Finished.........");
        } catch (IOException e) {
            log.error("Error occurred while downloading CSV File.");
            log.error(e.getMessage());
        }
    }

    @GetMapping("/replication")
    public void replication(HttpServletResponse response) throws IOException {
        try {

            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
            String original = "This is technically my third sentence.";
            String paraphrase = "The above sentence is technically not the third sentence.";

            DatasetObject idList = new DatasetObject();

            idList.setOriginalId(databaseService.storeOriginalParagraph(original));
            idList.setParaphraseId(databaseService.storeParaphrasedParagraph(paraphrase));

            repo.save(idList);
            service.exportToCsv(responseWrapper);

            log.info("Download Successful.");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
