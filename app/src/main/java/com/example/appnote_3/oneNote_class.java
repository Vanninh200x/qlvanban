package com.example.appnote_3;

public class oneNote_class {
    private int id;
    private String textV_title, textV_content, textV_day, textV_time;
    private byte[] img_font;
    private String textV_updateTime;

    private String soHieuVB, loaivanban, trangthai, coquan, nguoiki;
    private String stringDuongDan;

    public oneNote_class(int id, String textV_title, String textV_content, String textV_day, String textV_time, byte[] img_font, String textV_updateTime, String soHieuVB, String loaivanban, String trangthai, String coquan, String nguoiki, String stringDuongDan) {
        this.id = id;
        this.textV_title = textV_title;
        this.textV_content = textV_content;
        this.textV_day = textV_day;
        this.textV_time = textV_time;
        this.img_font = img_font;
        this.textV_updateTime = textV_updateTime;
        this.soHieuVB = soHieuVB;
        this.loaivanban = loaivanban;
        this.trangthai = trangthai;
        this.coquan = coquan;
        this.nguoiki = nguoiki;
        this.stringDuongDan = stringDuongDan;
    }

    public oneNote_class(int id, String textV_title, String textV_content, String textV_day, String textV_time, byte[] img_font, String textV_updateTime) {
        this.id = id;
        this.textV_title = textV_title;
        this.textV_content = textV_content;
        this.textV_day = textV_day;
        this.textV_time = textV_time;
        this.img_font = img_font;
        this.textV_updateTime = textV_updateTime;
    }



    public String getSoHieuVB() {
        return soHieuVB;
    }

    public void setSoHieuVB(String soHieuVB) {
        this.soHieuVB = soHieuVB;
    }

    public String getLoaivanban() {
        return loaivanban;
    }

    public void setLoaivanban(String loaivanban) {
        this.loaivanban = loaivanban;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getCoquan() {
        return coquan;
    }

    public void setCoquan(String coquan) {
        this.coquan = coquan;
    }

    public String getNguoiki() {
        return nguoiki;
    }

    public void setNguoiki(String nguoiki) {
        this.nguoiki = nguoiki;
    }

    public String getStringDuongDan() {
        return stringDuongDan;
    }

    public void setStringDuongDan(String stringDuongDan) {
        this.stringDuongDan = stringDuongDan;
    }



    public String getTextV_updateTime() {
        return textV_updateTime;
    }

    public void setTextV_updateTime(String textV_updateTime) {
        this.textV_updateTime = textV_updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextV_title() {
        return textV_title;
    }

    public void setTextV_title(String textV_title) {
        this.textV_title = textV_title;
    }

    public String getTextV_content() {
        return textV_content;
    }

    public void setTextV_content(String textV_content) {
        this.textV_content = textV_content;
    }

    public String getTextV_time() {
        return textV_time;
    }

    public void setTextV_time(String textV_time) {
        this.textV_time = textV_time;
    }

    public String getTextV_day() {
        return textV_day;
    }

    public void setTextV_day(String textV_day) {
        this.textV_day = textV_day;
    }

    public byte[] getImg_font() {
        return img_font;
    }

    public void setImg_font(byte[] img_font) {
        this.img_font = img_font;
    }
}
