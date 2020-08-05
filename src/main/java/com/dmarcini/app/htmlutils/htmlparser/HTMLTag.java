package com.dmarcini.app.htmlutils.htmlparser;

public enum HTMLTag {
    TITLE ("title");

    private final String lowerCaseTagName;

    HTMLTag(String lowerCaseTagName) {
        this.lowerCaseTagName = lowerCaseTagName;
    }

    public String toLowerCase() {
        return lowerCaseTagName;
    }
}
