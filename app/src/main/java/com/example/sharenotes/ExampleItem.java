package com.example.sharenotes;

public class ExampleItem {
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUrl() {
        return url;
    }
    public int getNo() {
        return no;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String courseId;
    private String url;
    private String uploader;
    private String desc;
    private int no;

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }

    private String fac;

    public ExampleItem(String courseId, String url,String fac, String desc,String uploader,int no) {
        this.courseId = courseId;
        this.url = url;
        this.uploader = uploader;
        this.desc = desc;
        this.fac = fac;
        this.no = no;
    }
}