package com.dmarcini.app.htmlparser;

public enum HTMLProperty {
    HREF ("href"),
    TITLE ("title");

    private final String lowerCasePropertyName;

    HTMLProperty(String lowerCasePropertyName) {
        this.lowerCasePropertyName = lowerCasePropertyName;
    }

    public String asLowerCase() {
        return lowerCasePropertyName;
    }
}
