package com.application.webScraping;

import com.application.webScraping.service.dataTransformer.DataTransformer;
import com.application.webScraping.service.webScrapp.WebScraper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;
@SpringBootApplication
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

//  @Bean
//  public CommandLineRunner run() {
//    return args -> {
//    long startTime = System.currentTimeMillis();
//
//    System.out.println("Acessing URL");
//    WebScraper webScraper1 = new WebScraper("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos");
//
//    Map<String, String> pdfLinks = new HashMap<>();
//
//    String[] pdfNames = new String[2];
//
//    pdfNames[0] = "anexoi";
//    pdfNames[1] = "anexoii";
//
//    System.out.println("Finding pdf links");
//    pdfLinks = webScraper1.findFilesLinksByName(pdfNames, "pdf");
//
//    for (String pdfLink : pdfLinks.keySet()) {
//      System.out.println(pdfLink);
//    }
//
//    System.out.println("Downloading pdf files");
//    webScraper1.downloadFilesByLink(pdfLinks, "pdf", "src/downloads");
//
//    Set<String> filesPath = webScraper1.listDirectoryFiles("src/downloads");
//
//    System.out.println("Ziping files");
//    webScraper1.zipFiles(filesPath, "anexos", "src/downloads");
//
//    DataTransformer dataTransformer = new DataTransformer();
//    System.out.println("Finding Pdf files");
//    String filePath = "src/downloads/anexoi.pdf";
//    String outputPath = "src/downloads/anexoi.csv";
//
//
//    dataTransformer.ExtractTableWithTabula(filePath, outputPath);
//    System.out.println("Pdf found in " + filePath);
//    System.out.println("Table extracted in: " + (System.currentTimeMillis() - startTime) + "ms");
//
//
//    String filePath2 = "src/downloads/anexoi.csv";
//    String outputPath2 = "src/downloads";
//    dataTransformer.zipFile(filePath2, "Teste_Yan_Carvalho", outputPath2);
//    System.out.println("File compressed in: " + outputPath2);
//
//    long endTime = System.currentTimeMillis();
//    System.out.println("Execution time: " + (endTime - startTime) + "ms");
//
//
//    WebScraper webScraper2 = new WebScraper("https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas/");
//    String[] pdfNames2 = new String[1];
//    pdfNames2[0] = "relatorio";
//    Map<String, String> pdfLinks2 = webScraper2.findFilesLinksByName(pdfNames2, "csv");
//    for (String pdfLink : pdfLinks2.keySet()) {
//      System.out.println(pdfLinks2.get(pdfLink));
//    }
//    webScraper2.downloadFilesByLink(pdfLinks2, "csv", "src/documents");
//
//    webScraper2 = new WebScraper("https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/2023/");
//    String reference = "2023";
//    pdfLinks2 = webScraper2.findFilesLinksByReference(reference, "zip");
//    for (String pdfLink : pdfLinks2.keySet()) {
//      System.out.println(pdfLinks2.get(pdfLink));
//    }
//    webScraper2.downloadFilesByLink(pdfLinks2, "zip", "src/documents/Demonstrações Contabeis 2023");
//
//    webScraper2 = new WebScraper("https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/2024/");
//    String reference2 = "2024";
//    pdfLinks2 = webScraper2.findFilesLinksByReference(reference2, "zip");
//    for (String pdfLink : pdfLinks2.keySet()) {
//      System.out.println(pdfLinks2.get(pdfLink));
//    }
//    webScraper2.downloadFilesByLink(pdfLinks2, "zip", "src/documents/Demonstrações Contabeis 2024");
//  };
//}
}
