package com.example.parly;

public class Upload_img {
    private String name;
    private String imageUrl;

    public Upload_img() {
    }

    Upload_img(String name, String imageUrl) {
        if(name.trim().equals("")){
            name="N Name";
        }
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }
}
