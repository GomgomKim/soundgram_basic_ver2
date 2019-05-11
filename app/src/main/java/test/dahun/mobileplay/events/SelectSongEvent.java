package test.dahun.mobileplay.events;

public class SelectSongEvent {
    public int position;

    public SelectSongEvent(int position){
        this.position = position;
    }

    public int getPosition(){
        return position;
    }
}
