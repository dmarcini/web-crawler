package com.dmarcini.app.htmlutils.htmlparser;

public enum HTMLProperty {
    HREF ("href"),
    TITLE ("title");

    private final String lowerCasePropertyName;

    HTMLProperty(String lowerCasePropertyName) {
        this.lowerCasePropertyName = lowerCasePropertyName;
    }

    public String toLowerCase() {
        return lowerCasePropertyName;
    }
}
