package cn.springmvc.model;

import java.io.Serializable;

public class Language implements Serializable {
    private String language;

    private static final long serialVersionUID = 1L;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }
}