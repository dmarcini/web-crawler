package com.dmarcini.app.htmlparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTMLParserTest {
    public final static String HTMLContent =
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "   <title>HTML Parser Test</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "   <h1>My First Heading</h1>\n" +
            "   <p>My first paragraph.</p>\n" +
            "</body>\n" +
            "</html>";

    @Test
    void getTagContent_GetHTMLTitle_ReturnCorrectTitle() {
        String title = HTMLParser.getTagContent(HTMLContent, HTMLTag.TITLE);

        Assertions.assertEquals("HTML Parser Test", title);
    }
}
