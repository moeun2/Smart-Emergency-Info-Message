package com.passta.a2ndproj.data;

public class CardVO {
    String name;
    int image;

    public CardVO(String name, int imageNumber){
        this.name = name;
        this.image = imageNumber;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImage(int image){
        this.image = image;
    }

    public String getName(){
        return name;
    }

    public int getImage(){
        return image;
    }
}
