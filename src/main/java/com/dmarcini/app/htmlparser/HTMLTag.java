package com.dmarcini.app.htmlparser;

public enum HTMLTag {
    TITLE ("title");

    private final String lowerCaseTagName;

    HTMLTag(String lowerCaseTagName) {
        this.lowerCaseTagName = lowerCaseTagName;
    }

    public String asLowerCase() {
        return lowerCaseTagName;
    }
}
