package test.dahun.mobileplay.events;

import java.util.ArrayList;

import test.dahun.mobileplay.database.SongData;

public class SongInfoEvent {
    ArrayList<SongData> song_arr;

    public SongInfoEvent(ArrayList<SongData> arr){
        this.song_arr = new ArrayList<>();
        this.song_arr = arr;
    }

    public ArrayList<SongData> getSong_arr(){
        return this.song_arr;
    }
}
