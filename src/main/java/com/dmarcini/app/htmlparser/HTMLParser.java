package com.dmarcini.app.htmlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    public static String getTagContents(String HTMLContents, HTMLTag HTMLTag) {
        Pattern pattern = Pattern.compile("<" + HTMLTag.asLowerCase() + "(?:| .*?)>" + "(.*?)" +
                                          "</" + HTMLTag.asLowerCase() + ">");
        Matcher matcher = pattern.matcher(HTMLContents);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    public static String getPropertyValue(String HTMLContents, HTMLProperty HTMLProperty) {
        Pattern pattern = Pattern.compile(HTMLProperty.asLowerCase() + "=[\"'](?://)?(.*?)[\"'][ >]");
        Matcher matcher = pattern.matcher(HTMLContents);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
