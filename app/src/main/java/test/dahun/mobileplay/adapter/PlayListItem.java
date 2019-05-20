package test.dahun.mobileplay.adapter;

import android.graphics.drawable.Drawable;

public class PlayListItem {
    private int type = 0; // 곡 : 0 , 앨범 소개 : 1

    private String index = "";
    private int heart;
    private int heart_num = 0;
    private String title = "";
    private String singer = "";
    private int isTitle = 0;
    private int isHeart = 0;

    private String intro_title = "";
    private String intro_content = "";

    private int song_id = 0;

    public PlayListItem(){}

    public void setType(int type){ this.type = type; }
    public int getType() { return this.type; }

    public void setIndex(String index){ this.index = index; }
    public String getIndex() { return this.index; }

    public void setHeart(int heart){ this.heart = heart; }
    public int getHeart() { return this.heart; }

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

    public void setIntro_title(String intro_title){ this.intro_title = intro_title; }
    public String getIntro_title() { return this.intro_title; }

    public void setIntro_content(String intro_content){ this.intro_content = intro_content; }
    public String getIntro_content() { return this.intro_content; }

    public void setSong_id(int song_id){ this.song_id = song_id; }
    public int getSong_id() { return this.song_id; }
}
