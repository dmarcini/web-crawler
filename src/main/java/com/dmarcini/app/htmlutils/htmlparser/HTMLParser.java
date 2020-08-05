package com.dmarcini.app.htmlutils.htmlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HTMLParser {
    public static String getTagContents(String htmlContents, HTMLTag htmlTag) {
        Pattern pattern = Pattern.compile("<" + htmlTag.toLowerCase() + ".*?>" + "(.*?)" +
                                          "</" + htmlTag.toLowerCase() + ">");
        Matcher matcher = pattern.matcher(htmlContents);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    public static String getPropertyValue(String htmlContents, HTMLProperty htmlProperty) {
        Pattern pattern = Pattern.compile(htmlProperty.toLowerCase() + "=[\"']((?://)?.*?)[\"'][ >]");
        Matcher matcher = pattern.matcher(htmlContents);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
