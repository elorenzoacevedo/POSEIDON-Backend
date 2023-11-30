package com.poseidon.inventory.service.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

@Slf4j
public class Printer {
    private final String PRINTER_NAME = "Xerox VersaLink B400";

    public void printPDF(Path filePath) {
        try {
            PDDocument document = Loader.loadPDF(filePath.toFile());
            PrintService printService = findPrintService();

            if (printService == null) {
                log.error("No printer found with name: {}", PRINTER_NAME);
                return;
            }

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            job.setPrintService(printService);
            job.print();
        } catch (IOException | PrinterException e) {
            log.error("There has been an error printing the PDF: {}", e.getMessage());
        }
    }
    public void printPNG(Path filePath) {
        try {
            File imageFile = filePath.toFile();
            if (!imageFile.exists() || !imageFile.isFile() || !imageFile.getName().toLowerCase().endsWith(".png")) {
                log.error("Invalid PNG file: {}", filePath);
                return;
            }

            PrintService printService = findPrintService();

            if (printService == null) {
                log.error("No printer found with name: {}", PRINTER_NAME);
                return;
            }

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(printService);

            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                    if (pageIndex != 0) {
                        return NO_SUCH_PAGE;
                    }

                    try {
                        // Read the PNG image
                        java.awt.Image image = ImageIO.read(imageFile);

                        // Draw the image on the printable area
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.drawImage(image, 250, 0, (int) pf.getImageableWidth() - 200, (int) pf.getImageableHeight() - 400, null);

                        return PAGE_EXISTS;
                    } catch (IOException e) {
                        log.error("Error reading PNG file: {}", e.getMessage());
                        return NO_SUCH_PAGE;
                    }
                }
            });

            job.print();
        } catch (PrinterException e) {
            log.error("There has been an error printing the PNG: {}", e.getMessage());
        }
    }

    private PrintService findPrintService() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(PRINTER_NAME)) {
                return printService;
            }
        }
        return null;
    }
}
