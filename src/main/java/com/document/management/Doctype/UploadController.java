package com.document.management.Doctype;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private OCRService ocrService;

    private ClassificationService classificationService;
    private ClassificationSpringBootService classificationSpringBootService;


    public UploadController(OCRService ocrService,
                            ClassificationService classificationService,
                            ClassificationSpringBootService classificationSpringBootService)
    {
        this.classificationService = classificationService;
        this.ocrService = ocrService;
        this.classificationSpringBootService = classificationSpringBootService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        File temp = File.createTempFile("uploaded_", ".pdf");
        file.transferTo(temp);

        String extractedText = ocrService.extractText(temp);
        //String classification = classificationService.classifyText(extractedText);
        String classification = classificationSpringBootService.classifyText(extractedText);
        logger.info(classification);

        return "redirect:/upload.html"; // send user back to form
    }



}
