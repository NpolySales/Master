package com.metrosoft.arafat.salebook.holder;

/**
 * Created by hp on 12/21/2017.
 */

public class IdName {
    private String id;
    private String name;
    private  String image_link;

    public IdName(String id) {
        this.id = id;
    }

    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public IdName(String id, String name, String image_link) {
        this.id = id;
        this.name = name;
        this.image_link = image_link;
    }

    public String getId() {
        return id;
    }



    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    @Override
    public String toString() {
        return "IdName [id=" + id + ", name=" + name + "]";
    }
}