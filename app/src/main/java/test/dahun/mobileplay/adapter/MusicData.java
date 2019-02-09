package test.dahun.mobileplay.adapter;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicData implements Parcelable {

    private String musicTitle;
    private String singer;
    private Uri musicImg;
    private String albumId;
    private String musicId;

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Uri getMusicImg() {
        return musicImg;
    }

    public void setMusicImg(Uri musicImg) {
        this.musicImg = musicImg;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(musicId);
        parcel.writeString(albumId);
        parcel.writeString(musicImg.toString());
        parcel.writeString(musicTitle);
        parcel.writeString(singer);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MusicData() {

    }

    public MusicData(Parcel in) {
        readFromParcel(in);
    }


    private void readFromParcel(Parcel in){
        musicId = in.readString();
        albumId = in.readString();
        musicImg = Uri.parse(in.readString());
        musicTitle = in.readString();
        singer = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MusicData createFromParcel(Parcel in) {
            return new MusicData(in);
        }

        public MusicData[] newArray(int size) {
            return new MusicData[size];
        }
    };
}

