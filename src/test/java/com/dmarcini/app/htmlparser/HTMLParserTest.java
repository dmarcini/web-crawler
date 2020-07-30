package com.dmarcini.app.htmlparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTMLParserTest {
    private final static String HTML_CONTENTS =
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

    private final static String TAG_CONTENTS =
            "<a id=\"test-id\" href=\"test-href\" " +
            "title=\"test-title\">";

    @Test
    void getTagContent_GetHTMLTitle_ReturnCorrectTitle() {
        String title = HTMLParser.getTagContents(HTML_CONTENTS, HTMLTag.TITLE);

        Assertions.assertEquals("HTML Parser Test", title);
    }

    @Test
    void getPropertyValue_GetNotLastInTagPropertyValue_ReturnCorrectValue() {
        String propertyValue = HTMLParser.getPropertyValue(TAG_CONTENTS, HTMLProperty.HREF);

        Assertions.assertEquals("test-href", propertyValue);
    }

    @Test
    void getPropertyValue_GetLastInTagPropertyValue_ReturnCorrectValue() {
        String propertyValue = HTMLParser.getPropertyValue(TAG_CONTENTS, HTMLProperty.TITLE);

        Assertions.assertEquals("test-title", propertyValue);
    }
}
