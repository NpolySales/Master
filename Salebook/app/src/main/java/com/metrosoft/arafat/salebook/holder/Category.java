package com.metrosoft.arafat.salebook.holder;

/**
 * Created by hp on 10/10/2017.
 */

public class Category {

    String catName;
    String catId;
    int catImageID;
    int isfav;
    int isturned;
    String parentCat;

    public int getIsturned() {
        return isturned;
    }

    public void setIsturned(int isturned) {
        this.isturned = isturned;
    }

    public int getIsfav() {
        return isfav;
    }

    public void setIsfav(int isfav) {
        this.isfav = isfav;
    }

    public String getCatName() {
        return catName;
    }
    public String getCatId() {
        return catId;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
    public void setCatId(String catId) {
        this.catId = catId;
    }
    public int getImageResourceId() {
        return catImageID;
    }

    public void setImageResourceId(int catImageID) {
        this.catImageID = catImageID;
    }
    public String getParentCat(){return  parentCat;};
    public void setParentCat(String parentCat){this.parentCat=parentCat;};


}
