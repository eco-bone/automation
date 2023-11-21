package com.dataset.automation.service;

import com.dataset.automation.dao.DatasetRepo;
import com.dataset.automation.dao.ProfessionsRepo;
import com.dataset.automation.dto.BotRequest;
import com.dataset.automation.dto.DatasetDto;
import com.dataset.automation.model.DatasetObject;
import com.dataset.automation.model.Profession;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
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

    @Autowired
    private final OpenAIChatService chatService;

    public AutomationServiceImpl(BotService botService, DatasetRepo datasetRepo,
                                 ProfessionsRepo profRepo, CrudService datasetService, OpenAIChatService chatService) {
        this.botService = botService;
        this.datasetRepo = datasetRepo;
        this.profRepo = profRepo;
        this.datasetService = datasetService;
        this.chatService = chatService;
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

    public void createDataset2(int n) {
        log.info("Dataset Creation Process Started.........");
        String[] profession = {"teacher", "carpenter", "pilot", "soldier"};
        for (int i = 0; i < n; i++) {
            String prompt = "Explain the top 5 skills required to be an outstanding " + profession[i] + " in about 500 words. " +
                    "After that, paraphrase the output. Provide the original answer and paraphrase separated by '==' which i can use as delimiter to split them. Dont provide the 'Original' and 'Paraphrased' headings for the paragraphs but keep the numbering and subheadings for the pointers.";
            String content = chatService.getChatCompletion(prompt);
            log.info("Number of Calls to API Completed: " + (i + 1));
            String[] sections = content.split("==");
            DatasetObject datasetObject = new DatasetObject();
            log.info("Saving the retrieved paragraphs...........");
            datasetObject.setOriginalId(datasetService.storeOriginalParagraph(sections[0]));
            datasetObject.setParaphraseId(datasetService.storeParaphrasedParagraph(sections[1]));

            datasetRepo.save(datasetObject);
            log.info("Number of dataset objects saved: " + (i + 1));
        }
    }

    public void createDataset3(int n) {

        String prompt = "";

        String content = chatService.getChatCompletion(prompt);

        String[] sections = content.split("==");

        for (int i = 0; i < sections.length; i++) {
            DatasetObject datasetObject = new DatasetObject();
            if (i == 0) {
                datasetObject.setOriginalId(datasetService.storeOriginalParagraph(sections[0]));
                datasetObject.setParaphraseId(datasetService.storeParaphrasedParagraph(sections[1]));
            } else if (i % 2 == 0) {
                datasetObject.setOriginalId(datasetService.storeOriginalParagraph(sections[i]));
                datasetObject.setParaphraseId(datasetService.storeParaphrasedParagraph(sections[i + 1]));
            }
            datasetRepo.save(datasetObject);
        }
    }


    public void createDataset4(int n){
        String prompt = "Provide the top 5 skills required to excel as a/an [profession].Describe each skill in around 150 words. Then, paraphrase this information to about 10 percent variation, but maintain the length of each skill description. Avoid using headings and separate the original and paraphrased versions using '==' as it is as a delimiter. Ensure the retention of the five skills and their respective descriptions' length. give the output for this for a teacher,biologist and driver.Ensure consistent formatting throughout the responses for each profession, regardless of the content or profession mentioned.";
        log.info("Interacting with openAI API...........");
        String content = chatService.getChatCompletion(prompt);
        log.info(content);
        log.info("Response Retrieved from API............");
        String[] sections = content.split("==");
        log.info("Response Extracted.........");
        int x = 0;
        for(int i=0;i<=5;i++){

            DatasetObject datasetObject = new DatasetObject();
            if(i==0){
                datasetObject.setOriginalId(datasetService.storeOriginalParagraph(sections[i]));
                datasetObject.setParaphraseId(datasetService.storeParaphrasedParagraph(sections[i+1]));
                x++;
            } else if(i>1 && i%2==1){
                datasetObject.setOriginalId(datasetService.storeOriginalParagraph(sections[i]));
                datasetObject.setParaphraseId(datasetService.storeParaphrasedParagraph(sections[i+1]));
                x++;
            }
            datasetRepo.save(datasetObject);
            log.info(x + "data objects saved");
        }

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


