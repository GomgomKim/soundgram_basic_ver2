package test.dahun.mobileplay.events;

public class GetSongPlayInfoEvent {
    public boolean is_play;
    public int duration;
    public int position;

    public GetSongPlayInfoEvent(boolean is_play, int duration, int position){
        this.is_play = is_play;
        this.duration = duration;
        this.position = position;
    }

    public int getDuration(){
        return duration;
    }

    public boolean getIsPlay(){
        return is_play;
    }

    public int getPosition(){
        return position;
    }

}
