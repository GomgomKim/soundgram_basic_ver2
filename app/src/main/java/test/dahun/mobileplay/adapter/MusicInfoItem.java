package test.dahun.mobileplay.adapter;

public class MusicInfoItem {
    private String music_title;
    private String composition;
    private String writer;
    private String arrangement;

    public MusicInfoItem(String m, String c, String w, String a){
        music_title = m;
        composition = c;
        writer = w;
        arrangement = a;
    }

    public void setMusic_title(String music_title){ this.music_title = music_title; }
    public String getMusic_title() { return this.music_title; }

    public void setComposition(String title){ this.composition = composition; }
    public String getComposition() { return this.composition; }

    public void setWriter(String title){ this.writer = writer; }
    public String getWriter() { return this.writer; }

    public void setArrangement(String title){ this.arrangement = arrangement; }
    public String getArrangement() { return this.arrangement; }
}
