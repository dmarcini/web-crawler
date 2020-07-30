package com.dmarcini.app.htmlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    public static String getTagContent(String HTMLContent, HTMLTag HTMLTag) {
        Pattern pattern = Pattern.compile("<" + HTMLTag.asLowerCase() + "(?:| .*?)>" + "(.*?)" +
                                          "</" + HTMLTag.asLowerCase() + ">");
        Matcher matcher = pattern.matcher(HTMLContent);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
