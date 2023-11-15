package com.dataset.automation.service;

import com.dataset.automation.dao.DatasetRepo;
import com.dataset.automation.dao.ProfessionsRepo;
import com.dataset.automation.dto.BotRequest;
import com.dataset.automation.dto.DatasetDto;
import com.dataset.automation.model.DatasetObject;
import com.dataset.automation.model.Profession;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AutomationServiceImpl implements AutomationService {

    @Autowired
    private final BotService botService;

    @Autowired
    private final DatasetRepo datasetRepo;

    @Autowired
    private final ProfessionsRepo profRepo;

    @Autowired
    private final CrudService datasetService;

    public AutomationServiceImpl(BotService botService, DatasetRepo datasetRepo,
                                 ProfessionsRepo profRepo, CrudService datasetService) {
        this.botService = botService;
        this.datasetRepo = datasetRepo;
        this.profRepo = profRepo;
        this.datasetService = datasetService;
    }


    public void createDataset(int n) {
        String getOriginalPrompt = "";
        String paraphrasePrompt = "\n";
        for (int i = 0; i < n; i++) {
            Profession prof = profRepo.findById(i).orElse(null);

            if (prof.getProfession() != null) {
                log.info("Profession found: " + prof.getProfession() + ".........");
                BotRequest request1 = new BotRequest();
                request1.setMessage(getOriginalPrompt + prof.getProfession());
                DatasetObject datasetObject = new DatasetObject();

                datasetObject.setOriginalId(datasetService.
                        storeOriginalParagraph(botService.getOutput(request1)));

                log.info("Original Paragraph Created for profession : " + prof.getProfession());

                BotRequest request2 = new BotRequest();

                request2.setMessage(paraphrasePrompt + datasetService.
                        retrieveOriginalParagraph(datasetObject.getOriginalId()));

                datasetObject.setParaphraseId(datasetService.
                        storeParaphrasedParagraph(botService.getOutput(request2)));

                log.info("Paraphrase Created for profession : " + prof.getProfession());
                datasetRepo.save(datasetObject);
                log.info("NUMBER OF PROFESSIONS ITERATED: " + i);
            } else {
                log.info("Profession not found.........");
            }
        }
        log.info("DATASET CREATION COMPLETE. TO USE DATASET, EXPORT TO CSV FILE.");
    }

    public List<DatasetObject> listDatasetObjects() {
        List<DatasetObject> datasetIDS = datasetRepo.findAll(Sort.by("id").ascending());
        return datasetIDS;
    }

        public void exportToCsv(HttpServletResponseWrapper response) throws IOException {
        log.info("Download Process Started......");
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        log.info("Date Set......");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=dataset_" + currentDateTime + ".csv";
        log.info("Headers Set.....");

        response.setHeader(headerKey, headerValue);
        List<DatasetObject> datasetIDS = this.listDatasetObjects();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        log.info("Csv Writer created.......");

        String[] csvHeader = {"original", "paraphrase"};
        String[] nameMapping = {"original", "paraphrase"};
        log.info("Csv Headers Created.....");

        csvWriter.writeHeader(csvHeader);
        log.info("Csv Headers Written....");

        for (DatasetObject datasetObject : datasetIDS) {
            DatasetDto datasetDto = new DatasetDto();
            datasetDto.setOriginal(datasetService.retrieveOriginalParagraph(datasetObject.getOriginalId()));
            datasetDto.setParaphrase(datasetService.retrieveParaphrasedParagraph(datasetObject.getParaphraseId()));
            csvWriter.write(datasetDto, nameMapping);
        }
        log.info("Database Mapped and downloaded to csv file.");

        csvWriter.close();
    }
}


