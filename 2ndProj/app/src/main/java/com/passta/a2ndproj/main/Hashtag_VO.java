package com.passta.a2ndproj.main;

public class Hashtag_VO {
    private String hashtagText;
    private int circleImageViewId;
    private boolean isClicked;

    public Hashtag_VO(String hashtagText, int circleImageViewId, boolean isClicked) {
        this.hashtagText = hashtagText;
        this.circleImageViewId = circleImageViewId;
        this.isClicked = isClicked;
    }

    public String getHashtagText() {
        return hashtagText;
    }

    public void setHashtagText(String hashtagText) {
        this.hashtagText = hashtagText;
    }

    public int getCircleImageViewId() {
        return circleImageViewId;
    }

    public void setCircleImageViewId(int circleImageViewId) {
        this.circleImageViewId = circleImageViewId;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

}
