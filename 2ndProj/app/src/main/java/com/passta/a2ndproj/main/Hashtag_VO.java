package com.passta.a2ndproj.main;

public class Hashtag_VO {
    private String hashtagText;
    private int circleImageViewId;

    public Hashtag_VO(String hashtagText, int circleImageViewId) {
        this.hashtagText = hashtagText;
        this.circleImageViewId = circleImageViewId;
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
}
