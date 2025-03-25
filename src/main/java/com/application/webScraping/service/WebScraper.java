package com.application.webScraping.service;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WebScraper {

    private String url;

    public WebScraper(String url) {
        this.url = url;
    }

    // Extrai todos os links de PDF da página e retorna um Map (texto do link -> URL absoluta)
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

    // Retorna links de PDF que correspondem aos nomes informados
    public Map<String, String> findPdfLinksByName(String[] pdfNames) {
        Map<String, String> pdfLinks = new HashMap<>();
        try {
            Document doc = Jsoup.connect(this.url).get();
            Elements links = findPdfLinks(doc);
            pdfLinks = matchPdfNames(pdfNames, links, pdfLinks);
            if(pdfLinks.isEmpty()) {
                throw new IOException("Nehum pdf encontrado, verifique se o nome foi digitado corretamente");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return pdfLinks;
    }

    // Remove diferenças de caixa e espaços para facilitar a comparação
    public static String normalize(String input) {
        return input.toLowerCase().replaceAll("\\s+", "");
    }

    // Associa os nomes informados aos links correspondentes
    public Map<String, String> matchPdfNames(String[] pdfNames, Elements links, Map<String, String> pdfLinks) {
        for (String pdfName : pdfNames) {
            String pdfNameLower = pdfName.toLowerCase();
            for (Element link : links) {
                if (normalize(link.text()).contains(normalize(pdfNameLower))) {
                    pdfLinks.put(pdfName, link.absUrl("href"));
                    break;
                }
            }
        }
        return pdfLinks;
    }

    // Efetua o download dos PDFs a partir dos links fornecidos e salva no diretório de saída
    public void DownloadPdfFilesByLink(Map<String, String> pdfLinks, String outputPath) {
        for (Map.Entry<String, String> entry : pdfLinks.entrySet()) {
            try {
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
    }

    //Recebe um Set de caminhos do diretorio e um caminho de saída do arquivo zip
    public void zipFiles(Set<String> filesPath, String outputPath) {
      try(FileOutputStream fileOutputStream = new FileOutputStream(outputPath+"/zipedFiles.zip");
          BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
          ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);) {


        for (String filePath : filesPath) {
            File file = new File(filePath);

            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);

            try(FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);) {

                byte[] buffer = new byte[16 * 1024]; //Utiliza um buffer de 16k para otimizar o desempenho
                while(bufferedInputStream.read(buffer) != -1) {
                    zipOutputStream.write(buffer, 0, buffer.length);
                }
            }

            zipOutputStream.closeEntry();
        }
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }

    //Recebe um caminho para um diretório e coleta os caminhos de todos os arquivos do diretório
    public Set<String> listDirectoryFiles(String directory) {
        return Stream.of(Objects.requireNonNull(new File(directory).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getAbsolutePath)
                .collect(Collectors.toSet());
    }

    // Seleciona elementos de links que terminam com .pdf no documento
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
