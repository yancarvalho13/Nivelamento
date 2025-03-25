package com.application.webScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WebScraper {

    private String url;

    public WebScraper(String url) {
        this.url = url;
    }
    public Map<String, String> findAllPdfLinks() {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements pdfLinks = doc.select("a[href$=.pdf]");

            return pdfLinks.stream()
                    .collect(Collectors.toMap(
                            Element::text,
                            link -> link.absUrl("href"),
                            (v1, v2) -> v1  // Em caso de chaves duplicadas, mantém o primeiro valor
                    ));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return new HashMap<>();
        }
    }


    public Map<String, String> findPdfLinksByName(String[] pdfNames) {
        Map<String, String> pdfLinks = new HashMap<>();
        try {
            Document doc = Jsoup.connect(this.url).get();
            Elements links = findPdfLinks(doc);
            matchPdfNames(links, pdfLinks, pdfNames);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return pdfLinks;
    }

    private static Elements findPdfLinks(Document doc) {
        return doc.select("a[href$=.pdf]");
    }

    private static void matchPdfNames(Elements links, Map<String, String> pdfLinks, String[] anexos) {
        for (Element link : links) {
            String text = link.text().toLowerCase();
            for (String anexo : anexos) {
                if (text.contains(anexo.toLowerCase()) && !pdfLinks.containsKey(anexo)) {
                    pdfLinks.put(anexo, link.absUrl("href"));
                }
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
