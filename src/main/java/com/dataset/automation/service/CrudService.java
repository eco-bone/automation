package com.dataset.automation.service;

import com.dataset.automation.model.DatasetObject;
import org.springframework.stereotype.Service;

@Service
public class CrudService {

    private final LargeTextService largeTextService;

    public CrudService(LargeTextService largeTextService) {
        this.largeTextService = largeTextService;
    }


    public Long storeOriginalParagraph(String originalText) {
        // Store the original paragraph using LargeTextService
        return largeTextService.createLargeObject(originalText);
    }

    public Long storeParaphrasedParagraph(String paraphrasedText) {
        // Store the paraphrased paragraph using LargeTextService
        return largeTextService.createLargeObject(paraphrasedText);
    }

    public void linkParagraphsToEntity(DatasetObject datasetObject, Long originalOid, Long paraphrasedOid) {
        // Link the OIDs to the entity
        datasetObject.setOriginalId(originalOid);
        datasetObject.setParaphraseId(paraphrasedOid);
    }

    public String retrieveOriginalParagraph(Long oid) {
        // Retrieve the original paragraph from the large object using LargeTextService
        return largeTextService.getLargeText(oid);
    }

    public String retrieveParaphrasedParagraph(Long oid) {
        // Retrieve the paraphrased paragraph from the large object using LargeTextService
        return largeTextService.getLargeText(oid);
    }
}
