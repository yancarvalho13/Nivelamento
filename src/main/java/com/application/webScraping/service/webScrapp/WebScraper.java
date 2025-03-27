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
  // Bloqueio para garantir que apenas um download seja realizado por vez
  private final Lock downloadLock = new ReentrantLock();
  // Flag para verificar se o download está em andamento
  private volatile boolean isDownloading = false;
  private String url;

  public WebScraper(String url) {
    this.url = url;
  }

  // Extrai todos os links de PDF da página
  // Retorna um mapa com o texto do link (nome do arquivo) e sua URL absoluta
  public Map<String, String> findAllPdfLinks() {
    try {
      // Conecta-se à página e obtém o conteúdo HTML
      Document doc = Jsoup.connect(url).get();
      // Seleciona todos os links que terminam com ".pdf"
      Elements pdfLinks = doc.select("a[href$=.pdf]");
      // Cria um mapa onde o texto do link é a chave e a URL é o valor
      return pdfLinks.stream()
              .collect(Collectors.toMap(
                      Element::text, // Texto do link (nome do arquivo)
                      link -> link.absUrl("href"), // URL do link
                      (v1, v2) -> v1 // Caso haja conflito, escolhe o primeiro valor
              ));
    } catch (IOException e) {
      // Em caso de erro, exibe uma mensagem de erro
      JOptionPane.showMessageDialog(null, e.getMessage());
      return new HashMap<>();
    }
  }

  // Busca links de arquivos específicos baseados em uma referência (parte do nome)
  // Retorna um mapa com o texto do link (nome do arquivo) e sua URL absoluta
  public Map<String, String> findFilesLinksByReference(String reference, String fileType) {
    Map<String, String> fileLinks = new HashMap<>();
    try {
      // Conecta-se à página e obtém o conteúdo HTML
      Document doc = Jsoup.connect(this.url).get();
      // Encontra todos os links com base no tipo de arquivo
      Elements links = findPdfLinks(doc,fileType);
      // Algoritmo de correspondência de nomes
      fileLinks = matchFileReference(reference, links, fileLinks);
      if (fileLinks.isEmpty()) {
        // Caso nenhum arquivo seja encontrado, lança uma exceção
        throw new IOException("Nenhum " + fileType + " encontrado, verifique se o nome foi digitado corretamente");
      }
    } catch (IOException e) {
      e.getMessage();
    }
    return fileLinks;
  }

  // Busca links de arquivos específicos baseados nos nomes dos arquivos
  // Retorna um mapa com o texto do link (nome do arquivo) e sua URL absoluta
  public Map<String, String> findFilesLinksByName(String[] pdfNames, String fileType) {
    Map<String, String> pdfLinks = new HashMap<>();
    try {
      // Conecta-se à página e obtém o conteúdo HTML
      Document doc = Jsoup.connect(this.url).get();
      // Encontra todos os links com base no tipo de arquivo
      Elements links = findPdfLinks(doc,fileType);
      // Algoritmo de correspondência de nomes
      pdfLinks = matchFileNames(pdfNames, links, pdfLinks);
      if (pdfLinks.isEmpty()) {
        // Caso nenhum arquivo seja encontrado, lança uma exceção
        throw new IOException("Nenhum " + fileType + " encontrado, verifique se o nome foi digitado corretamente");
      }
    } catch (IOException e) {
      e.getMessage();
    }
    return pdfLinks;
  }

  // Método de normalização de strings, removendo espaços e convertendo para minúsculas
  public static String normalize(String input) {
    return input.toLowerCase().replaceAll("\\s+", "");
  }

  // Algoritmo complexo de correspondência de nomes de PDF
  // Realiza busca flexível, comparando nomes parcialmente
  // A correspondência é feita ignorando maiúsculas e minúsculas, e espaços
  public Map<String, String> matchFileNames(String[] pdfNames, Elements links, Map<String, String> pdfLinks) {
    for (String pdfName : pdfNames) {
      String pdfNameLower = pdfName.toLowerCase();
      for (Element link : links) {
        // Verifica se o nome do arquivo contém o nome procurado
        if (normalize(link.text()).contains(normalize(pdfNameLower))) {
          // Adiciona a URL do arquivo ao mapa, com o nome fornecido
          pdfLinks.put(pdfName, link.absUrl("href"));
          break;
        }
      }
    }
    return pdfLinks;
  }

  // Algoritmo de correspondência de nomes baseado em referência fornecida
  // Realiza busca flexível usando uma referência (nome parcial) para encontrar os links
  // Os arquivos encontrados são adicionados ao mapa com contagem numérica no nome
  public Map<String, String> matchFileReference(String reference, Elements links, Map<String, String> pdfLinks) {
    String pdfNameLower = reference.toLowerCase();
    int counter = 0;
    for (Element link : links) {
      // Verifica se o nome do arquivo contém a referência procurada
      if (normalize(link.text()).contains(normalize(pdfNameLower))) {
        counter++;
        // Adiciona a URL do arquivo ao mapa, com a referência e contador
        pdfLinks.put(reference + "." + counter, link.absUrl("href"));
      }
    }
    return pdfLinks;
  }

  // Método de download de arquivos a partir dos links encontrados
  // Usa canais de I/O para transferência eficiente de dados
  public void downloadFilesByLink(Map<String, String> pdfLinks, String fileType, String outputPath) {
    downloadLock.lock();
    try {
      // Define que o download está em andamento
      isDownloading = true;
      // Itera sobre os links de arquivos e realiza o download
      for (Map.Entry<String, String> entry : pdfLinks.entrySet()) {
        try {
          // Cria um canal de leitura para o arquivo remoto
          ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(entry.getValue()).openStream());
          // Cria um canal de escrita no sistema de arquivos local
          FileOutputStream fileOutputStream = new FileOutputStream(outputPath + "/" + entry.getKey() + "." + fileType);
          FileChannel fileChannel = fileOutputStream.getChannel();
          // Transfere os dados do canal remoto para o local
          fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
          fileOutputStream.close();
          fileChannel.close();
          readableByteChannel.close();
          System.out.println("Downloading: " + entry.getValue());
          System.out.println("Download completed");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } finally {
      // Marca o fim do processo de download
      isDownloading = false;
      downloadLock.unlock();
    }
  }

  // Método para compactar arquivos em um arquivo ZIP
  // Utiliza buffer de leitura e escrita otimizado para desempenho
  public void zipFiles(Set<String> filesPath, String fileName, String outputPath) {
    downloadLock.lock();
    try {
      // Aguarda até que o download termine antes de continuar
      while (isDownloading) {
        Thread.sleep(100);
      }
      String zipFilePath = outputPath + "/" + fileName + ".zip";

      // Cria o arquivo ZIP de saída
      try (FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
           ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

        for (String filePath : filesPath) {
          File file = new File(filePath);
          // Cria uma entrada no arquivo ZIP para cada arquivo
          ZipEntry zipEntry = new ZipEntry(file.getName());
          zipOutputStream.putNextEntry(zipEntry);

          try (FileInputStream fileInputStream = new FileInputStream(file);
               BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            // Usa um buffer de 16KB para otimizar a leitura e escrita
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

  // Método para listar arquivos em um diretório específico
  // Utiliza a API Stream para iterar e filtrar os arquivos
  public Set<String> listDirectoryFiles(String directory) {
    downloadLock.lock();
    try {
      // Aguarda até que o download termine antes de continuar
      while (isDownloading) {
        Thread.sleep(100);
      }

      // Retorna um conjunto de arquivos no diretório (não diretórios)
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

  // Método para selecionar os links de arquivos de um tipo específico
  public Elements findPdfLinks(Document doc, String fileType) {
    return doc.select("a[href$=." + fileType + "]");
  }

  // Getters e setters para a URL
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
