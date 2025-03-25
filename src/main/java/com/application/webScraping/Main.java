package com.application.webScraping;

import com.application.webScraping.service.WebScraper;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        WebScraper webScraper = new WebScraper("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos");
        Map<String,String> pdfLinks= new HashMap<>();
        String [] pdfNames = new String[2];
        pdfNames[0] = "anexoi";
        pdfNames[1] = "anexoii";
        pdfLinks = webScraper.findPdfLinksByName(pdfNames);
        System.out.println(pdfLinks);
        long startTime = System.currentTimeMillis();
        webScraper.DownloadPdfFilesByLink(pdfLinks,"src/main/resources/downloads");
        long endTime = System.currentTimeMillis();
        System.out.println("Download Time: " + (endTime - startTime) + "ms");
    }
}
