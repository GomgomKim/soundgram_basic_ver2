package test.dahun.mobileplay.adapter;

import android.graphics.drawable.Drawable;

public class PlayListItem {
    private String index;
    private Drawable heart;
    private int heart_num;
    private String title;
    private String singer;
    private int isTitle = 0;
    private int isHeart = 0;

    public void setIndex(String index){ this.index = index; }
    public String getIndex() { return this.index; }

    public void setHeart(Drawable heart){ this.heart = heart; }
    public Drawable getHeart() { return this.heart; }

    public void setHeart_num(int heart_num){ this.heart_num = heart_num; }
    public int getHeart_num() { return this.heart_num; }

    public void setTitle(String title){ this.title = title; }
    public String getTitle() { return this.title; }

    public void setSinger(String singer){ this.singer = singer; }
    public String getSinger() { return this.singer; }

    public void setIsTitle(int isTitle){ this.isTitle = isTitle; }
    public int getIsTitle() { return this.isTitle; }

    public void setIsHeart(int isHeart){ this.isHeart = isHeart; }
    public int getIsHeart() { return this.isHeart; }
}
