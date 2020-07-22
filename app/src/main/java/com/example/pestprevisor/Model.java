package com.example.pestprevisor;

public class Model {
    private String title, description,pesticides;
    private int img;

    public Model(){}

    public Model(String title, String description,String pesticides, int img){
        this.title = title;
        this.description = description;
        this.pesticides = pesticides;
        this.img = img;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPesticides() {
        return pesticides;
    }

    public void setPesticides(String pesticides) { this.pesticides = pesticides;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

