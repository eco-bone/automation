package com.dataset.automation.service;

import com.dataset.automation.dao.DatasetRepo;
import com.dataset.automation.dao.ProfessionsRepo;
import com.dataset.automation.dto.BotRequest;
import com.dataset.automation.dto.DatasetDto;
import com.dataset.automation.model.DatasetObject;
import com.dataset.automation.model.Profession;
import jakarta.servlet.http.HttpServletResponseWrapper;
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
    private final OpenAIChatService apiService;

    @Autowired
    private final OpenAIChatService2 apiService2;

    public AutomationServiceImpl(BotService botService, DatasetRepo datasetRepo,
                                 ProfessionsRepo profRepo, CrudService datasetService, OpenAIChatService apiService, OpenAIChatService2 apiService2) {
        this.botService = botService;
        this.datasetRepo = datasetRepo;
        this.profRepo = profRepo;
        this.datasetService = datasetService;
        this.apiService = apiService;
        this.apiService2 = apiService2;
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

    public void createDataset2(int num) {
        long startTime = System.currentTimeMillis();
        int x = 0;
        log.info("Dataset Creation process started....");
        String[] professions1 = {"Renewable Energy Planner",
                "Environmental Psychologist",
                "Biomedical Statistician",
                "Holistic Wellness Coach",
                "Quantum Biologist",
                "Sustainable Materials Engineer",
                "Cryptocurrency Trader",
                "Space Tourism Specialist",
                "Astrocartographer",
                "Quantum Physicist",
                "Renewable Energy Scientist",
                "Urban Sustainability Planner",
                "Blockchain Analyst",
                "Environmental Educator",
                "Wildlife Photographer",
                "Astrogeologist",
                "Renewable Energy Specialist",
                "Quantum Computing Analyst",
                "Space Tour Guide",
                "Holistic Fashion Designer",
                "Environmental Lawyer",
                "Cryptocurrency Developer",
                "Marine Biotechnologist",
                "Space Exploration Advocate",
                "Astrophysics Blogger",
                "Animal Welfare Advocate",
                "Quantum Computing Scientist",
                "Sustainable Urban Designer",
                "Renewable Energy Technician",
                "Blockchain Consultant",
                "Astrophysics Illustrator",
                "Holistic Nutrition Consultant",
                "Animal Behavior Specialist",
                "Cryptocurrency Strategist",
                "Quantum Physicist",
                "Space Tourism Manager",
                "Sustainable Agriculture Entrepreneur",
                "Environmental Health Specialist",
                "Animal Conservationist",
                "Digital Currency Analyst",
                "Sustainable Agriculture Specialist",
                "Ethical Fashion Stylist",
                "Blockchain Developer",
                "Marine Biotechnologist",
                "Cryptocurrency Developer",
                "Space Exploration Advocate",
                "Astrophysics Blogger",
                "Animal Welfare Advocate",
                "Quantum Computing Scientist",
                "Sustainable Urban Designer",
                "Renewable Energy Technician",
                "Holistic Nutrition Consultant",
                "Astrophysics Illustrator",
                "Space Tourism Coordinator",
                "Blockchain Analyst",
                "Quantum Physicist",
                "Environmental Educator",
                "Wildlife Photographer",
                "Renewable Energy Planner",
                "Cryptocurrency Trader",
                "Sustainable Energy Consultant",
                "Astroecologist",
                "Digital Forensic Analyst",
                "Space Tourism Specialist",
                "Quantum Biologist",
                "Biomedical Statistician",
                "Holistic Wellness Coach",
                "Cryptocurrency Developer",
                "Environmental Psychologist",
                "Renewable Energy Project Manager",
                "Space Settlement Designer",
                "Astrocartographer",
                "Blockchain Developer",
                "Quantum Physicist",
                "Sustainable Materials Engineer",
                "Cryptocurrency Strategist",
                "Wildlife Conservationist",
                "Sustainable Agriculture Entrepreneur",
                "Environmental Economist",
                "Space Tourism Coordinator",
                "Quantum Computing Engineer",
                "Renewable Energy Scientist",
                "Holistic Fashion Designer",
                "Blockchain Analyst",
                "Animal Welfare Advocate",
                "Astrophysics Blogger",
                "Sustainable Urban Designer",
                "Cryptocurrency Developer",
                "Marine Biotechnologist",
                "Quantum Computing Scientist",
                "Space Exploration Advocate",
                "Sustainable Agriculture Specialist",
                "Astrophysics Illustrator",
                "Environmental Health Specialist",
                "Blockchain Consultant",
                "Holistic Nutrition Consultant",
                "Renewable Energy Technician",
                "Animal Behavior Specialist",
                "Space Tourism Manager",
                "Quantum Physicist",
                "Digital Currency Analyst",
                "Sustainable Energy Consultant",
                "Cryptocurrency Trader",
                "Astrophysics Blogger",
                "Wildlife Photographer",
                "Renewable Energy Planner",
                "Space Tourism Specialist",
                "Blockchain Developer",
                "Cryptocurrency Developer",
                "Sustainable Materials Engineer",
                "Quantum Biologist",
                "Holistic Wellness Coach",
                "Environmental Psychologist",
                "Biomedical Statistician",
                "Space Settlement Designer",
                "Blockchain Analyst",
                "Astrocartographer",
                "Quantum Physicist",
                "Animal Welfare Advocate",
                "Sustainable Agriculture Entrepreneur",
                "Renewable Energy Project Manager",
                "Environmental Economist",
                "Cryptocurrency Strategist",
                "Space Tourism Coordinator",
                "Astrophysics Illustrator",
                "Holistic Fashion Designer",
                "Digital Forensic Analyst",
                "Quantum Computing Engineer",
                "Blockchain Developer",
                "Sustainable Urban Designer",
                "Cryptocurrency Developer",
                "Space Exploration Advocate",
                "Marine Biotechnologist",
                "Astrophysics Blogger",
                "Animal Welfare Advocate",
                "Renewable Energy Scientist",
                "Holistic Nutrition Consultant",
                "Quantum Physicist",
                "Environmental Health Specialist",
                "Wildlife Conservationist",
                "Blockchain Analyst",
                "Sustainable Agriculture Specialist",
                "Cryptocurrency Trader",
                "Space Tourism Manager",
                "Renewable Energy Technician",
                "Cryptocurrency Trader",
                "Holistic Nutrition Consultant",
                "Quantum Biologist",
                "Renewable Energy Planner",
                "Animal Behavior Specialist",
                "Environmental Psychologist",
                "Blockchain Analyst",
                "Sustainable Urban Designer",
                "Astrophysics Blogger",
                "Wildlife Conservationist",
                "Astrophysics Illustrator",
                "Digital Currency Analyst",
                "Sustainable Agriculture Specialist",
                "Environmental Economist",
                "Blockchain Developer",
                "Space Tourism Specialist",
                "Renewable Energy Technician",
                "Animal Welfare Advocate",
                "Blockchain Consultant",
                "Quantum Computing Scientist",
                "Holistic Fashion Designer",
                "Sustainable Materials Engineer",
                "Space Tourism Manager",
                "Astrophysics Blogger"};

        String[] professions2 = {"Quantum Computing Engineer",
                "Blockchain Developer",
                "Astrophysics Blogger",
                "Space Tourism Specialist",
                "Cryptocurrency Analyst",
                "Animal Welfare Advocate",
                "Sustainable Agriculture Specialist",
                "Holistic Wellness Coach",
                "Renewable Energy Project Manager",
                "Quantum Physicist",
                "Astrophysics Illustrator",
                "Environmental Psychologist",
                "Blockchain Analyst",
                "Digital Currency Analyst",
                "Space Exploration Advocate",
                "Marine Biotechnologist",
                "Cryptocurrency Developer",
                "Holistic Fashion Designer",
                "Sustainable Urban Designer",
                "Quantum Computing Scientist",
                "Animal Behavior Specialist",
                "Space Settlement Designer",
                "Blockchain Consultant",
                "Renewable Energy Scientist",
                "Cryptocurrency Trader",
                "Astrophysics Blogger",
                "Environmental Economist",
                "Holistic Nutrition Consultant",
                "Sustainable Materials Engineer",
                "Space Tourism Manager",
                "Animal Welfare Advocate",
                "Digital Currency Analyst",
                "Quantum Physicist",
                "Renewable Energy Technician",
                "Blockchain Developer",
                "Cryptocurrency Strategist",
                "Astrophysics Illustrator",
                "Space Exploration Advocate",
                "Environmental Health Specialist",
                "Holistic Wellness Coach",
                "Animal Behavior Specialist",
                "Cryptocurrency Trader",
                "Quantum Computing Engineer",
                "Renewable Energy Planner",
                "Sustainable Agriculture Specialist",
                "Holistic Fashion Designer",
                "Astrophysics Illustrator",
                "Animal Behavior Specialist",
                "Blockchain Consultant",
                "Quantum Computing Scientist",
                "Environmental Psychologist",
                "Space Settlement Designer",
                "Holistic Wellness Coach",
                "Biomedical Statistician",
                "Sustainable Materials Engineer",
                "Cryptocurrency Developer",
                "Quantum Biologist",
                "Astrocartographer",
                "Renewable Energy Planner",
                "Blockchain Developer",
                "Space Tourism Specialist",
                "Cryptocurrency Strategist",
                "Holistic Fashion Designer",
                "Environmental Economist",
                "Digital Currency Analyst",
                "Sustainable Agriculture Entrepreneur",
                "Animal Welfare Advocate",
                "Blockchain Analyst",
                "Astrophysics Blogger",
                "Quantum Physicist",
                "Space Tourism Coordinator",
                "Renewable Energy Project Manager",
                "Cryptocurrency Developer",
                "Marine Biotechnologist",
                "Environmental Health Specialist",
                "Wildlife Photographer",
                "Holistic Nutrition Consultant",
                "Astrophysics Illustrator",
                "Quantum Computing Engineer",
                "Sustainable Urban Designer",
                "Animal Behavior Specialist",
                "Blockchain Developer",
                "Space Exploration Advocate",
                "Sustainable Energy Consultant",
                "Cryptocurrency Trader",
                "Renewable Energy Scientist",
                "Environmental Psychologist",
                "Blockchain Analyst",
                "Astrocartographer",
                "Quantum Physicist",
                "Space Settlement Designer",
                "Holistic Wellness Coach",
                "Biomedical Statistician",
                "Cryptocurrency Developer",
                "Astrophysics Blogger",
                "Cryptocurrency Strategist",
                "Sustainable Materials Engineer",
                "Space Tourism Manager",
                "Renewable Energy Technician",
                "Animal Welfare Advocate",
                "Blockchain Consultant",
                "Quantum Computing Scientist",
                "Holistic Fashion Designer",
                "Wildlife Conservationist",
                "Astrophysics Illustrator",
                "Digital Currency Analyst",
                "Sustainable Agriculture Specialist",
                "Environmental Economist",
                "Blockchain Developer",
                "Space Tourism Specialist",
                "Cryptocurrency Trader",
                "Holistic Nutrition Consultant",
                "Quantum Biologist",
                "Renewable Energy Planner",
                "Animal Behavior Specialist",
                "Environmental Psychologist",
                "Blockchain Analyst",
                "Sustainable Urban Designer",
                "Astrophysics Blogger",
                "Astrophysics Illustrator",
                "Animal Behavior Specialist",
                "Blockchain Consultant",
                "Quantum Computing Scientist",
                "Environmental Psychologist",
                "Space Settlement Designer",
                "Holistic Wellness Coach",
                "Biomedical Statistician",
                "Sustainable Materials Engineer",
                "Cryptocurrency Developer",
                "Quantum Biologist",
                "Astrocartographer",
                "Renewable Energy Planner",
                "Blockchain Developer",
                "Space Tourism Specialist",
                "Cryptocurrency Strategist",
                "Holistic Fashion Designer",
                "Environmental Economist",
                "Digital Currency Analyst",
                "Sustainable Agriculture Entrepreneur",
                "Animal Welfare Advocate",
                "Blockchain Analyst",
                "Astrophysics Blogger",
                "Quantum Physicist",
                "Space Tourism Coordinator",
                "Renewable Energy Project Manager",
                "Cryptocurrency Developer",
                "Marine Biotechnologist",
                "Environmental Health Specialist",
                "Wildlife Photographer",
                "Holistic Nutrition Consultant",
                "Astrophysics Illustrator",
                "Quantum Computing Engineer",
                "Sustainable Urban Designer",
                "Animal Behavior Specialist",
                "Blockchain Developer",
                "Space Exploration Advocate",
                "Sustainable Energy Consultant",
                "Cryptocurrency Trader",
                "Renewable Energy Scientist",
                "Environmental Psychologist",
                "Blockchain Analyst",
                "Astrocartographer",
                "Quantum Physicist",
                "Space Settlement Designer",
                "Holistic Wellness Coach",
                "Biomedical Statistician",
                "Cryptocurrency Developer",
                "Cryptocurrency Strategist"};


        for (int i = 0; i < professions1.length; i++) {
            String professionA = professions1[i];
            String professionB = professions2[i];
            String prompt1 = "Explain the top 5 skills required to be an outstanding " + professionA + " in about 500 words. Then paraphrase the entire 500 words while retaining the numbered points and the number of words." +
                    "Don't leave lines anywhere. If one point ends, start the next point from the very next line. Separate the original and paraphrase using a delimiter '=='.";

            String prompt2 = "Explain the top 5 skills required to be an outstanding " + professionB + " in about 500 words. Then paraphrase the entire 500 words while retaining the numbered points and the number of words." +
                    "Don't leave lines anywhere. If one point ends, start the next point from the very next line. Separate the original and paraphrase using a delimiter '=='.";
            try {
                String content1 = apiService.getChatCompletion(prompt1);
                log.info("Data retrieved for entry for profession " + professionA);
                String[] sections1 = content1.split("""
                        ==

                        """);
                DatasetObject datasetObject = new DatasetObject();
                datasetObject.setOriginalId(datasetService.storeOriginalParagraph(sections1[0]));
                log.info("saved original for " + professionA);
                datasetObject.setParaphraseId(datasetService.storeParaphrasedParagraph(sections1[1]));
                log.info("saved paraphrase for " + professionA);

                datasetRepo.save(datasetObject);
                x++;
                log.info("Dataset entry saved for profession " + professionA);
                log.info("Time elapsed: " + (System.currentTimeMillis() - startTime) / 1000);
                log.info("Dataset Entries Saved: " + x);


                String content2 = apiService2.getChatCompletion(prompt2);
                log.info("Data retrieved for entry for profession " + professionB);
                String[] sections2 = content2.split("""
                        ==

                        """);
                DatasetObject datasetObject2 = new DatasetObject();
                datasetObject2.setOriginalId(datasetService.storeOriginalParagraph(sections2[0]));
                log.info("saved original for " + professionB);
                datasetObject2.setParaphraseId(datasetService.storeParaphrasedParagraph(sections2[1]));
                log.info("saved paraphrase for " + professionB);

                datasetRepo.save(datasetObject2);
                x++;
                log.info("Dataset entry saved for profession " + professionB);
                log.info("Time elapsed: " + (System.currentTimeMillis() - startTime) / 1000);
                log.info("Dataset Entries Saved: " + x);

            } catch (Exception e) {
                log.error("Error occured while making api call.....");
                log.error(e.getMessage());
                log.info("moving to next profession...");
            }
        }
        long timeElapsed = System.currentTimeMillis() - startTime;
        log.info("Time taken to complete all professions : " + String.valueOf(timeElapsed / 1000) + " seconds.");
    }

    public void createDataset3(int num) {
        long startTime = System.currentTimeMillis();
        int x = 0;
        log.info("Dataset Creation process started....");
        String[] profession = {
                "Kombucha Brewer", "Lighthouse Keeper", "Metal Sculptor", "Nanotechnology Engineer", "Opera Singer",
                "Palliative Care Nurse", "Quantum Astrophysicist", "Robotics Technician", "Sound Effects Artist", "Taxidermy Supply Shop Owner",
                "Urban Explorer", "Virtual Reality Architect", "Wood Carver", "Xenophotographer", "Yoga Retreat Planner",
                "Zipline Course Designer", "Amusement Park Imagineer", "Brewery Tour Guide", "Crystal Healer", "Dungeon Master",
                "Eco-friendly Fashion Designer", "Foley Mixer", "Gemstone Cutter", "Harbor Master", "Interior Green Wall Designer",
                "Jewelry Metalsmith", "Kinetic Typographer", "Landscape Ecologist", "Music Therapist", "Nautical Archaeologist",
                "Oyster Farmer", "Puppetry Therapist", "Quantum Ethicist", "Railroad Engineer", "Safari Guide",
                "Taxidermy Veterinarian", "Underwater Cave Explorer", "Vocal Coach", "Wine Biodynamic Consultant", "Xerophyte Biologist",
                "Yacht Interior Designer", "Zen Meditation Instructor", "Alpaca Farmer", "Bionic Limb Designer", "Cryptocurrency Analyst",
                "Drones Operator", "Exoplanet Scientist", "Feng Shui Planner", "Galactic Historian", "Holographic Programmer",
                "Insect Farmer", "Jellyfish Biologist", "Kite Aerial Photographer", "Laser Show Designer", "Martial Arts Therapist",
                "Nuclear Fusion Engineer", "Optical Illusion Artist", "Pixel Artist", "Quantum Information Scientist", "Reiki Master",
                "Solar System Explorer", "Time Travel Theorist", "Underwater Robotics Engineer", "Virtual Reality Sound Engineer", "Wine Glass Artist",
                "Xylophonist", "Yoga Philosophy Teacher", "Zero Gravity Trainer", "Astrodynamics Engineer", "Bonsai Horticulturist",
                "Culinary Tour Guide", "Dream Therapist", "Environmental Graphic Designer", "Feng Shui Consultant", "Galactic Travel Agent",
                "Hybrid Car Engineer", "Ice Cream Flavor Inventor", "Jazz Saxophonist", "Kite Surfing Instructor", "Luthier Apprentice",
                "Nanoengineering Specialist", "Outdoor Adventure Guide", "Paleoanthropologist", "Quantum Neurologist", "Remote Sensing Scientist",
                "Sustainable Architect", "Tattoo Artist", "Ultrasonographer", "Venomous Snake Milker", "Wine Grape Geneticist",
                "X-ray Crystallographer", "Yoga Trapeze Instructor", "Zebra Trainer", "Astrological Counselor", "Brewpub Owner",
                "Cryptocurrency Trader", "Dog Surfing Instructor", "Equestrian Masseuse", "Foley Recordist", "Gemstone Geologist",
                "Heliophysicist", "Insect Taxidermy Instructor", "Jewelry Appraisal Specialist", "Kelp Forest Guide", "Linguistic Archaeologist",
                "Mushroom Forager", "Neon Sign Designer", "Olfactory Branding Specialist", "Piano Tuner", "Quantum Psychophysiologist",
                "Rocket Scientist", "Synchronized Swimmer", "Taxidermy Artisan", "Urbex Artist", "Virtual Reality Game Developer",
                "Wine Harvester", "Xenobotanical Researcher", "Yoga Anatomy Instructor", "Zipline Course Inspector", "Airship Pilot",
                "Biodiversity Conservationist", "Cocktail Historian", "Digital Nomad Coach", "Ethical Fashion Stylist", "Fire Lookout",
                "Glass Blowing Instructor", "Hypnosis Therapist", "Internet of Things (IoT) Developer", "Jazz Dance Instructor", "Kombucha Mixologist",
                "Linguistic Engineer", "Meteor Shower Chaser", "Noodle Artist", "Ornithologist", "Puppeteer Therapist",
                "Quantum Theorist", "Robotics Researcher", "Sound Healing Therapist", "Taxidermy Art Conservationist", "Underwater Basket Weaver",
                "Vocal Sound Engineer", "Wine Label Designer", "Xenozoologist", "Yoga Retreat Photographer", "Zipline Guide Trainer",
                "Amusement Park Ride Tester", "Brewmaster Apprentice", "Crystallographer", "Dungeon Master Event Planner", "Eco-friendly Product Designer",
                "Flower Arrangement Therapist", "Gemstone Inspector", "Harbor Master", "Interactive Exhibit Designer", "Jewelry Engraver",
                "Kinetic Sculpture Artist", "Landscape Photographer", "Music Festival Organizer", "Nature Sound Recordist", "Oyster Shucker",
                "Puppetry Workshop Facilitator", "Quantum Physicist", "Railroad Conductor", "Sailboat Captain", "Tea Blender",
                "Taxidermy Art Restoration Specialist", "Underwater Filmmaker", "Vocational Rehabilitation Counselor", "Wine Label Illustrator", "Xerophyte Horticulturist",
                "Yoga Studio Owner", "Zen Garden Designer", "Alpine Guide", "Bioplastics Engineer", "Cryptography Expert",
                "Drone Racing Pilot", "Exoplanet Hunter", "Feng Shui Consultant", "Galactic Tour Guide", "Holographic Artist",
                "Insect Behaviorist", "Jellyfish Bioluminescence Specialist", "Kite Designer", "Laser Safety Officer", "Material Scientist",
                "Nuclear Medicine Technologist", "Optical Illusion Specialist", "Pixel Art Animator", "Quantum Computing Engineer", "Reiki Practitioner",
                "Solar Technician", "Time Travel Historian", "Underwater Photographer", "Virtual Reality Cinematographer", "Wine Label Consultant",
                "Xenobotanist", "Yoga Therapist", "Zero Gravity Flight Instructor", "Astrodynamics Specialist", "Bonsai Master",
                "Culinary Historian", "Dream Analyst", "Environmental Economist", "Feng Shui Specialist", "Galactic Ethnographer",
                "Hydroponic Farmer", "Ice Carver", "Jazz Trombonist", "Kinetic Sculpture Designer", "Lucid Dream Therapist",
                "Meme Archivist", "Nanochemistry Researcher", "Ocular Prosthesis Technician", "Paper Engineer", "Quantum Chemistry Professor",
                "Recreational Therapist", "Solar Energy Consultant", "Taxidermy Art Restoration Specialist", "Underwater Welder", "Virtual Reality Filmmaker",
                "Wine Educator", "Xenobiological Engineer", "Yogurt Artisan", "Zen Gardener"
        };


        for (int i = 0; i < profession.length; i++) {
            String professionB = profession[i];
            String prompt2 = "Explain the top 5 skills required to be an outstanding " + professionB + " in about 500 words. Then paraphrase the entire 500 words while retaining the numbered points and the number of words." +
                    "Don't leave lines anywhere. If one point ends, start the next point from the very next line. Separate the original and paraphrase using a delimiter '=='.";
            String content2 = apiService2.getChatCompletion(prompt2);
            log.info("Data retrieved for entry for profession " + professionB);
            String[] sections2 = content2.split("""
                    ==

                    """);
            DatasetObject datasetObject2 = new DatasetObject();
            datasetObject2.setOriginalId(datasetService.storeOriginalParagraph(sections2[0]));
            log.info("saved original for " + professionB);
            datasetObject2.setParaphraseId(datasetService.storeParaphrasedParagraph(sections2[1]));
            log.info("saved paraphrase for " + professionB);

            datasetRepo.save(datasetObject2);
            x++;
            log.info("Dataset entry saved for profession " + professionB);
            log.info("Time elapsed: " + (System.currentTimeMillis() - startTime) / 1000);
            log.info("Dataset Entries Saved: " + x);
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


