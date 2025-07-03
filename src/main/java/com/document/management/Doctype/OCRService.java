package com.document.management.Doctype;


import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


@Service
public class OCRService {
    public String extractText(File pdfFile) throws Exception {
        PDDocument document = Loader.loadPDF(pdfFile);
        PDFRenderer renderer = new PDFRenderer(document);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
        tesseract.setLanguage("eng+fra+deu+bul+por");

        StringBuilder fullText = new StringBuilder();

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage image = renderer.renderImageWithDPI(page, 300);
            File imgFile = File.createTempFile("page_", ".png");
            ImageIO.write(image, "png", imgFile);

            fullText.append(tesseract.doOCR(imgFile)).append("\n");

            imgFile.delete();
        }

        document.close();
        String text = fullText.toString();
        return text;
    }
}
