package com.example.parly;

public class User {
    private String id;
    private String username;
    private String imgUrl;
    private String coverUrl;
    private String bio;
    private String background;
    private String status;
    private String compte;

    public User(String id, String username, String bio, String imgUrl, String coverUrl, String background,String status,String compte) {
        this.id = id;
        this.username = username;
        this.imgUrl = imgUrl;
        this.coverUrl = coverUrl;
        this.bio = bio;
        this.background = background;
        this.status=status;
        this.compte=compte;
    }



    public String getCompte() {
        return compte;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User() {
    }


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getBio() {
        return bio;
    }

    public String getBackground() {
        return background;
    }
}
