package test.dahun.mobileplay.database;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import test.dahun.mobileplay.events.IsPlayEvent;
import test.dahun.mobileplay.events.SongInfoEvent;
import test.dahun.mobileplay.services.BusProvider;

public class ParseSongData{

    String json_data = "";
    ArrayList<SongData> songDataArr;

    public ParseSongData(){

    }

    public ParseSongData(String data){
        this.json_data = data;
    }

    public void parseData(){
        try {
            songDataArr = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json_data);
            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int song_id = jsonObject.getInt("id");
                int album_id = jsonObject.getInt("album");
                String song_name = jsonObject.getString("song_name");
                String song_file_name = jsonObject.getString("song_file_name");
                String song_lyric = jsonObject.getString("song_lyric");
                int song_order = jsonObject.getInt("song_order");
                String compos_song = jsonObject.getString("msg1");
                String compos_lyric = jsonObject.getString("msg2");
                String remake_song = jsonObject.getString("msg3");
                int likeCount = jsonObject.getInt("likeCount");
                int isTitle = jsonObject.getInt("isTitle");

                Log.i("parse_data", "song id :"+song_id);
                Log.i("parse_data", "album_id :"+album_id);
                Log.i("parse_data", "song_name :"+song_name);
                Log.i("parse_data", "song_file_name :"+song_file_name);
                Log.i("parse_data", "song_lyric:"+song_lyric);

                songDataArr.add(new SongData(song_id, album_id, song_name, song_file_name, song_lyric, song_order, compos_song, compos_lyric, remake_song, likeCount, isTitle));
            }

            BusProvider.getInstance().post(new SongInfoEvent(songDataArr));

            } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
