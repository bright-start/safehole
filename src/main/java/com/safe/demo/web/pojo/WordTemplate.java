package com.safe.demo.web.pojo;

public class WordTemplate {
    private Integer wordTemplateId;

    private String wordName;

    private String templateDate;

    public Integer getWordTemplateId() {
        return wordTemplateId;
    }

    public void setWordTemplateId(Integer wordTemplateId) {
        this.wordTemplateId = wordTemplateId;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName == null ? null : wordName.trim();
    }

    public String getTemplateDate() {
        return templateDate;
    }

    public void setTemplateDate(String templateDate) {
        this.templateDate = templateDate == null ? null : templateDate.trim();
    }
}