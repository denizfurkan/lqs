package com.example.codingtr.lolquery.Management;

public class News {

    private int id;
    private String title;
    private String category;
    private String author;
    private String image;
    private String link;

    public News(int id, String title, String category, String author, String image, String link) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.author = author;
        this.image = image;
        this.link = link;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
