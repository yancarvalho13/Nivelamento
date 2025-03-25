package com.application.webScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebScraper {

    private String url;

    public WebScraper(String url) {
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
