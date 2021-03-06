package com.appsmata.qtoa.models;

public class ListingModel {
    private String header;
    private String subHeader;
    private String type;

    public ListingModel(String header, String subHeader, String type) {
        this.header = header;
        this.subHeader = subHeader;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSubHeader() {
        return subHeader;
    }

    public void setSubHeader(String subHeader) {
        this.subHeader = subHeader;
    }
}
