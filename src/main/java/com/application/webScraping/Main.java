package com.application.webScraping;

import com.application.webScraping.service.WebScraper;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        WebScraper webScraper = new WebScraper("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos");
        Map<String,String> pdfLinks= new HashMap<>();
        pdfLinks = webScraper.findAllPdfLinks();
        System.out.println(pdfLinks);
    }
}
