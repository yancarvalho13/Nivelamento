package com.application.webScraping.service.webScrapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WebScraper {
  private final Lock downloadLock = new ReentrantLock();
  private volatile boolean isDownloading = false;
  private String url;

  public WebScraper(String url) {
    this.url = url;
  }

  // Extrai todos os links de PDF da página
  // Retorna um mapa com o texto do link e sua URL absoluta
  public Map<String, String> findAllPdfLinks() {
    try {
      Document doc = Jsoup.connect(url).get();
      Elements pdfLinks = doc.select("a[href$=.pdf]");
      return pdfLinks.stream()
              .collect(Collectors.toMap(
                      Element::text,
                      link -> link.absUrl("href"),
                      (v1, v2) -> v1
              ));
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
      return new HashMap<>();
    }
  }

  // Encontra links de PDF correspondentes aos nomes informados
  // Realiza busca case-insensitive e parcial dos nomes de PDF
  public Map<String, String> findPdfLinksByName(String[] pdfNames) {
    Map<String, String> pdfLinks = new HashMap<>();
    try {
      Document doc = Jsoup.connect(this.url).get();
      Elements links = findPdfLinks(doc);
      // Algoritmo de correspondência de nomes
      pdfLinks = matchPdfNames(pdfNames, links, pdfLinks);
      if (pdfLinks.isEmpty()) {
        throw new IOException("Nehum pdf encontrado, verifique se o nome foi digitado corretamente");
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return pdfLinks;
  }

  // Remove diferenças de caixa e espaços para facilitar comparação de strings
  public static String normalize(String input) {
    return input.toLowerCase().replaceAll("\\s+", "");
  }

  // Algoritmo complexo de correspondência de nomes de PDF
  // Busca flexível que encontra PDFs mesmo com nomes parcialmente correspondentes
  public Map<String, String> matchPdfNames(String[] pdfNames, Elements links, Map<String, String> pdfLinks) {
    for (String pdfName : pdfNames) {
      String pdfNameLower = pdfName.toLowerCase();
      for (Element link : links) {
        // Verifica se o nome normalizado do link contém o nome procurado
        if (normalize(link.text()).contains(normalize(pdfNameLower))) {
          pdfLinks.put(pdfName, link.absUrl("href"));
          break;
        }
      }
    }
    return pdfLinks;
  }

  // Método de download de PDFs com tratamento de múltiplos arquivos
  // Utiliza canais de I/O para transferência eficiente de dados
  public void DownloadPdfFilesByLink(Map<String, String> pdfLinks, String outputPath) {
    downloadLock.lock();
    try {
      isDownloading = true;
      for (Map.Entry<String, String> entry : pdfLinks.entrySet()) {
        try {
          // Transferência de arquivo utilizando canais NIO para melhor performance
          ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(entry.getValue()).openStream());
          FileOutputStream fileOutputStream = new FileOutputStream(outputPath + "/" + entry.getKey() + ".pdf");
          FileChannel fileChannel = fileOutputStream.getChannel();
          fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
          fileOutputStream.close();
          fileChannel.close();
          readableByteChannel.close();
          System.out.println("Download completed");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } finally {
      isDownloading = false;
      downloadLock.unlock();
    }
  }

  // Método de compressão de arquivos em ZIP
  // Implementa compressão com buffer otimizado para performance
  public void zipFiles(Set<String> filesPath, String outputPath) {
    downloadLock.lock();
    try {
      // Aguarda conclusão de downloads em andamento
      while (isDownloading) {
        Thread.sleep(100);
      }

      try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath + "/zipedFiles.zip");
           ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

        for (String filePath : filesPath) {
          File file = new File(filePath);

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
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      downloadLock.unlock();
      System.out.println("Zip files compressed!");
    }
  }

  // Método para listar arquivos em um diretório
  // Utiliza Stream API para processamento eficiente
  public Set<String> listDirectoryFiles(String directory) {
    downloadLock.lock();
    try {
      while (isDownloading) {
        Thread.sleep(100);
      }

      return Stream.of(Objects.requireNonNull(new File(directory).listFiles()))
              .filter(file -> !file.isDirectory())
              .map(File::getAbsolutePath)
              .collect(Collectors.toSet());

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      downloadLock.unlock();
    }
  }

  public Elements findPdfLinks(Document doc) {
    return doc.select("a[href$=.pdf]");
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}