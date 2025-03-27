package com.application.webScraping;

import com.application.webScraping.repository.OperadoraFinanceiroRepository;
import com.application.webScraping.service.OperadoraSaudeService;
import com.application.webScraping.service.dataTransformer.DataTransformer;
import com.application.webScraping.service.operadoraFinanceiro.OperadoraFinanceiroService;
import com.application.webScraping.service.webScrapp.WebScraper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StartupRunner implements CommandLineRunner {

  private final OperadoraFinanceiroService operadoraFinanceiroService;
  private final OperadoraFinanceiroRepository operadoraFinanceiroRepository;
  private final OperadoraSaudeService operadoraSaudeService;

  public StartupRunner(OperadoraFinanceiroService operadoraFinanceiroService,
                       OperadoraFinanceiroRepository operadoraFinanceiroRepository, OperadoraSaudeService operadoraSaudeService) {
    this.operadoraFinanceiroService = operadoraFinanceiroService;
    this.operadoraFinanceiroRepository = operadoraFinanceiroRepository;
    this.operadoraSaudeService = operadoraSaudeService;
  }

  @Override
  public void run(String... args) {
    long startTime = System.currentTimeMillis();

//    processAnexoIPdf();
//    processRelatoriosOperadoras();
//    processDemonstraçõesContabeis();
      processarDadosOperadoras("src/documents/relatorio.csv");


    long endTime = System.currentTimeMillis();
    System.out.println("Execução finalizada em: " + (endTime - startTime) + "ms");
  }

  private void processAnexoIPdf() {
    try {
      WebScraper webScraper = new WebScraper("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos");

      String[] pdfNames = {"anexoi", "anexoii"};
      Map<String, String> pdfLinks = webScraper.findFilesLinksByName(pdfNames, "pdf");

      System.out.println("Links dos anexos:");
      pdfLinks.forEach((name, link) -> System.out.println(link));

      webScraper.downloadFilesByLink(pdfLinks, "pdf", "src/downloads");

      Set<String> filesPath = webScraper.listDirectoryFiles("src/downloads");
      webScraper.zipFiles(filesPath, "anexos", "src/downloads");

      String filePath = "src/downloads/anexoi.pdf";
      String outputPath = "src/downloads/anexoi.csv";

      DataTransformer dataTransformer = new DataTransformer();
      dataTransformer.ExtractTableWithTabula(filePath, outputPath);

      System.out.println("PDF convertido em tabela CSV com sucesso.");

      dataTransformer.zipFile(outputPath, "Teste_Yan_Carvalho", "src/downloads");
    } catch (Exception e) {
      System.err.println("Erro ao processar Anexo I: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void processRelatoriosOperadoras() {
    try {
      WebScraper webScraper = new WebScraper("https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas/");

      String[] fileNames = {"relatorio"};
      Map<String, String> csvLinks = webScraper.findFilesLinksByName(fileNames, "csv");

      System.out.println("Links dos relatórios:");
      csvLinks.values().forEach(System.out::println);

      webScraper.downloadFilesByLink(csvLinks, "csv", "src/documents");
    } catch (Exception e) {
      System.err.println("Erro ao baixar relatórios de operadoras: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void processDemonstraçõesContabeis() {
    try {
      downloadContabeisPorAno("2023");
      downloadContabeisPorAno("2024");

      processarArquivosContabeis("2023", List.of("2t2023.csv", "3T2023.csv", "4T2023.csv"));
      processarArquivosContabeis("2024", List.of("1T2024.csv", "2T2024.csv", "3T2024.csv", "4T2024.csv"));

    } catch (Exception e) {
      System.err.println("Erro ao processar demonstrações contábeis: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void downloadContabeisPorAno(String ano) throws Exception {
    WebScraper scraper = new WebScraper("https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/" + ano + "/");
    Map<String, String> links = scraper.findFilesLinksByReference(ano, "zip");

    System.out.println("Links das demonstrações contábeis " + ano + ":");
    links.values().forEach(System.out::println);

    scraper.downloadFilesByLink(links, "zip", "src/documents/Demonstrações Contabeis " + ano);
  }

  private void processarArquivosContabeis(String ano, List<String> arquivos) {
    String basePath = "src/documents/Demonstrações Contabeis " + ano + "/" + ano + "/";
    for (String arquivo : arquivos) {
      String pathCompleto = basePath + arquivo;
      System.out.println("Importando CSV: " + pathCompleto);
      operadoraFinanceiroService.extractCsvData(pathCompleto);
    }
  }

  private void processarDadosOperadoras(String path) {
    operadoraSaudeService.extractCsvData(path);
  }
}
