package com.application.webScraping.service.dataTransformer;

import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DataTransformer {
  private final Lock writingLock = new ReentrantLock();
  private volatile boolean isTransforming = false;
  public DataTransformer() {
  }

  public void ExtractTableWithTabula(String filePath, String outputPath){
    writingLock.lock();
    // Utiliza try-with-resources para garantir o fechamento dos streams e documentos
    try(InputStream inputStream = new FileInputStream(filePath);
        PDDocument document = PDDocument.load(inputStream);
        FileWriter fileWriter = new FileWriter(outputPath);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

      isTransforming = true;

      // Algoritmo de extração de planilhas (Tabula) que interpreta a estrutura tabular do PDF
      SpreadsheetExtractionAlgorithm seaAlgorithm = new SpreadsheetExtractionAlgorithm();
      PageIterator pageIterator = new ObjectExtractor(document).extract();
      StringBuilder stringBuilder = new StringBuilder();

      // Itera por cada página do documento
      System.out.println("Extracting table...");
      while (pageIterator.hasNext()) {
        Page page = pageIterator.next();
        List<Table> table = seaAlgorithm.extract(page);
        // Itera por cada tabela encontrada na página
        for(Table tables : table){
          List<List<RectangularTextContainer>> rows = tables.getRows();

          // Itera por cada linha da tabela
          for(List<RectangularTextContainer> row : rows){

            // Processa cada célula da linha
            for(RectangularTextContainer cell : row){
              // Mapeia as palavras a serem substituídas por seus respectivos valores
              Map<String, String> targetReplacement = new HashMap<>();
              targetReplacement.put("OD", "Seg. Odontológica");
              targetReplacement.put("AMB", "Seg. Ambulatorial");
              // Chama o método que formata o texto da célula aplicando as substituições
              String cellText = formatCellTextWithReplacement(cell.getText(), targetReplacement);
              stringBuilder.append(cellText).append(",");
            }
            // Remove a última vírgula da linha e adiciona uma quebra de linha
            stringBuilder.setLength(stringBuilder.length()-1);
            stringBuilder.append("\n");
          }
        }
      }
      // Escreve o conteúdo processado no arquivo de saída
      bufferedWriter.write(stringBuilder.toString());
      System.out.println("Table written to " + outputPath);
    } catch (Exception e ){
      // Verifica se o erro é de arquivo não encontrado para exibir uma mensagem específica
      if(e instanceof FileNotFoundException){
        System.err.println("Arquivo não encontrado, se estiver baixando o arquivo, aguarde o Download terminar para chamar o metodo!");
      }
      e.printStackTrace();
    }finally {
      isTransforming = false;
      writingLock.unlock();
    }
  }

  // Método que formata o texto de uma célula substituindo palavras-chave
  private String formatCellTextWithReplacement(String text, Map<String,String> namesToReplace) {
    String formattedText = text.replace("\r", " ")
            .replace(",", ";");
    // Itera sobre cada par chave/valor do Map e realiza a substituição com regex,
    // garantindo que a chave seja tratada como palavra isolada.
    for (Map.Entry<String, String> entry : namesToReplace.entrySet()) {
      String key = entry.getKey();
      String replacement = entry.getValue();
      formattedText = formattedText.replaceAll("\\b" + Pattern.quote(key) + "\\b", replacement);
    }
    return formattedText;
  }

  public void zipFile(String filePath, String fileName, String outputPath) {
    writingLock.lock();

    try {
      // Aguarda conclusão de downloads em andamento
      while (isTransforming) {
        Thread.sleep(100);
      }
      String zipFilePath = outputPath + "/" + fileName + ".zip";

      try (FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
           ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

        File file = new File(filePath);

        System.out.println("Compressing file " + file.getName());
        // Cria entrada para cada arquivo no ZIP
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zipOutputStream.putNextEntry(zipEntry);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

          // Buffer de 16k para otimização de performance de leitura/escrita
          byte[] buffer = new byte[16 * 1024];
          while (bufferedInputStream.read(buffer) != -1) {
            zipOutputStream.write(buffer, 0, buffer.length);
          }
        }

        zipOutputStream.closeEntry();

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      writingLock.unlock();
      System.out.println("Zip file compressed!");
    }
  }

  // Método auxiliar para formatação simples do texto da célula (sem substituições)
  private String formatCellText(String text) {
    return text.replace("\r", " ")
            .replace(",", ";");
  }
}
