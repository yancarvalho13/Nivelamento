package com.application.webScraping;

import com.application.webScraping.service.dataTransformer.DataTransformer;
import com.application.webScraping.service.webScrapp.WebScraper;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;

import java.util.*;

public class Main {
  public static void main(String[] args) {

    long startTime = System.currentTimeMillis();
    System.out.println("Acessing URL");
    WebScraper webScraper = new WebScraper("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos");

    Map<String, String> pdfLinks = new HashMap<>();

    String[] pdfNames = new String[2];

    pdfNames[0] = "anexoi";
    pdfNames[1] = "anexoii";

    System.out.println("Finding pdf links");
    pdfLinks = webScraper.findPdfLinksByName(pdfNames);

    for (String pdfLink : pdfLinks.keySet()) {
      System.out.println(pdfLink);
    }

    System.out.println("Downloading pdf files");
    webScraper.DownloadPdfFilesByLink(pdfLinks, "src/main/resources/downloads");

    Set<String> filesPath= webScraper.listDirectoryFiles("src/main/resources/downloads");

    System.out.println("Ziping files");
    webScraper.zipFiles(filesPath,"anexos" ,"src/main/resources/downloads");

    System.out.println("Finding Pdf files");
    DataTransformer dataTransformer = new DataTransformer();
    String filePath = "src/main/resources/downloads/anexoi.pdf";
    String outputPath = "src/main/resources/downloads/anexoi.csv";


    dataTransformer.ExtractTableWithTabula(filePath,outputPath);
    System.out.println("Pdf found in " + filePath);
    System.out.println("Table extracted in: "+ (System.currentTimeMillis() - startTime)+"ms");



    String filePath2 = "src/main/resources/downloads/anexoi.csv";
    String outputPath2 = "src/main/resources/downloads";
    dataTransformer.zipFile(filePath2,"Teste_Yan_Carvalho",outputPath2);
    System.out.println("File compressed in: "+ outputPath2);

    long endTime = System.currentTimeMillis();
    System.out.println("Execution time: " + (endTime - startTime) + "ms");

  }
}
