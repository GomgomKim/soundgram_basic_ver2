package test.dahun.mobileplay.database;

public class SongData {
    private int song_id;
    private int album_id;
    private String song_name;
    private String song_file_name;
    private String song_lyric;
    int song_order;
    String compos_song;
    String compos_lyric;
    String remake_song;
    int likeCount;
    int isTitle;

    public SongData(int s_id, int a_id, String s_name, String file, String lyric, int song_order, String compos_song, String compos_lyric, String remake_song, int likeCount, int isTitle){
        this.song_id = s_id;
        this.album_id = a_id;
        this.song_name = s_name;
        this.song_file_name = file;
        this.song_lyric = lyric;
        this.song_order = song_order;
        this.compos_song = compos_song;
        this.compos_lyric = compos_lyric;
        this.remake_song = remake_song;
        this.likeCount = likeCount;
        this.isTitle = isTitle;
    }

    public int getLikeCount(){
        return this.likeCount;
    }

    public int getSong_id(){ return this.song_id; }

}
