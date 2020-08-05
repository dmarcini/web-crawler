package com.dmarcini.app.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CrawledPage extends Page {
    private String title;
    private final Set<String> links;

    public CrawledPage(String url, int depth) {
        super(url, depth);

        this.links = new HashSet<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getLinks() {
        return links;
    }

    public void addLink(String link) {
        links.add(link);
    }
}
