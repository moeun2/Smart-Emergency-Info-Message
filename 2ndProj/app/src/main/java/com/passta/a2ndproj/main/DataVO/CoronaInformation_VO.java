package com.passta.a2ndproj.main.DataVO;

public class CoronaInformation_VO {
    private String text;
    private int imgNumber;
    private String http;

    public CoronaInformation_VO(String text, int imgNumber, String http) {
        this.text = text;
        this.imgNumber = imgNumber;
        this.http = http;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImgNumber() {
        return imgNumber;
    }

    public void setImgNumber(int imgNumber) {
        this.imgNumber = imgNumber;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }
}
