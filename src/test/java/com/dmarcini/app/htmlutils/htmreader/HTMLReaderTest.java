package com.dmarcini.app.htmlutils.htmreader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class HTMLReaderTest {
    private final static String HTML_CONTENTS =
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "   <title>HTML Parser Test</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "   <h1>My First Heading</h1>\n" +
            "</body>\n" +
            "</html>";

    @Test
    void getNextTag_GetTagsFromHTML_ReturnCorrectTags() throws NoSuchFieldException, IllegalAccessException {
        HTMLReader htmlReader = new HTMLReader(HTML_CONTENTS);

        Field htmlContents = HTMLReader.class.getDeclaredField("htmlContents");

        htmlContents.setAccessible(true);
        htmlContents.set(htmlReader, HTML_CONTENTS);

        Assertions.assertAll(
                "tag",
                () -> Assertions.assertEquals("<title>HTML Parser Test</title>", htmlReader.getNextTag()),
                () -> Assertions.assertEquals("<h1>My First Heading</h1>", htmlReader.getNextTag())
        );
    }
}
