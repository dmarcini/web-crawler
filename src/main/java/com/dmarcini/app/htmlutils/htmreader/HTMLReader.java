package com.dmarcini.app.htmlutils.htmreader;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HTMLReader {
    private static final String DEFAULT_REGEX = "(<([a-z]{1,6}[0-9]{0,1}).*?>.*?</\\2>)";

    private final String htmlContents;
    private int lastTagPos;
    private final Pattern pattern;

    public HTMLReader(String url) {
        this(url, DEFAULT_REGEX);
    }

    public HTMLReader(String url, String regex) {
        this.htmlContents = readHTML(url);
        this.lastTagPos = 0;
        this.pattern = Pattern.compile(regex);
    }

    public boolean hasNextTag() {
        if (htmlContents == null) {
            return false;
        }

        return pattern.matcher(htmlContents).find(lastTagPos);
    }

    public String getNextTag() {
        if (htmlContents == null) {
            return "";
        }

        Matcher matcher = pattern.matcher(htmlContents);
        String tag = "";

        if (matcher.find(lastTagPos)) {
            tag = matcher.group(1);
            lastTagPos = htmlContents.lastIndexOf(tag) + tag.length();
        }

        return tag;
    }

    private String readHTML(String url) {
        final StringBuilder stringBuilder = new StringBuilder();

        try  {
            URLConnection urlConnection = new URL(url).openConnection();

            if (!urlConnection.getContentType().contains("text/html")) {
                return "";
            }

            Scanner scanner = new Scanner(urlConnection.getInputStream(), StandardCharsets.UTF_8.toString());

            scanner.useDelimiter("\\A");

            while (scanner.hasNext()) {
                stringBuilder.append(scanner.next());
            }

        } catch (IOException ignore) { }

        return stringBuilder.toString();
    }
}
