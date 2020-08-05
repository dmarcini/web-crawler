package com.dmarcini.app.workers;

import com.dmarcini.app.domain.CrawledPage;
import com.dmarcini.app.domain.Page;
import com.dmarcini.app.htmlutils.htmlparser.HTMLParser;
import com.dmarcini.app.htmlutils.htmlparser.HTMLProperty;
import com.dmarcini.app.htmlutils.htmlparser.HTMLTag;
import com.dmarcini.app.htmlutils.htmreader.HTMLReader;

import java.lang.Math;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
    private final static int TASKS_CAPACITY = (int) Math.pow(2, 16);

    private final BlockingQueue<Page> tasks;
    private final Set<CrawledPage> crawledPages;
    private final int maxDepth;

    public Worker(String initialTask, int maxDepth) {
        this.tasks = new ArrayBlockingQueue<>(TASKS_CAPACITY);
        this.crawledPages = new LinkedHashSet<>();
        this.maxDepth = maxDepth;

        this.tasks.add(new Page(initialTask, 0));
    }

    @Override
    public void run() {
        while (true) {
            try {
                crawlPage(tasks.take());
            } catch (InterruptedException | MalformedURLException ignore) {
               return;
            }
        }
    }

    public synchronized Set<CrawledPage> getCrawledPages() {
        return crawledPages;
    }

    private void crawlPage(Page page) throws InterruptedException, MalformedURLException {
        HTMLReader htmlReader = new HTMLReader(page.getUrl());

        CrawledPage crawledPage = new CrawledPage(page.getUrl(), page.getDepth());

        while (htmlReader.hasNextTag()) {
            String tag = htmlReader.getNextTag();

            trySetTitle(crawledPage, tag);
            tryAddLink(crawledPage, tag, page.getDepth());
        }

        synchronized (crawledPages) {
            crawledPages.add(crawledPage);
        }
    }

    private void trySetTitle(CrawledPage crawledPage, String tag) {
        String title = HTMLParser.getTagContents(tag, HTMLTag.TITLE);

        boolean wasTitleSet = crawledPage.getTitle() != null;
        boolean isTitleFound = !title.isEmpty();

        if (!wasTitleSet && isTitleFound) {
            crawledPage.setTitle(title);
        }
    }

    private void tryAddLink(CrawledPage crawledPage, String tag, int depth) throws InterruptedException {
        String href = HTMLParser.getPropertyValue(tag, HTMLProperty.HREF);

        if (!href.isEmpty()) {
            boolean wasPageCrawled = crawledPages.contains(href);
            boolean isCorrectDepth = depth + 1 <= maxDepth;

            if (!wasPageCrawled && isCorrectDepth) {
                tasks.put(new Page(href, depth + 1));
            }

            crawledPage.addLink(href);
        }
    }
}
