package ru.solodkov.voipadmin.autoprovisioning.domain;

public class ConfigurationFile {

    protected String fileName;

    private byte[] content;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
