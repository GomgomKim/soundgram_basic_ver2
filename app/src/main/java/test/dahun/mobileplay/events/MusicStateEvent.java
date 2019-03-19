package test.dahun.mobileplay.events;

public class MusicStateEvent {
    public int duration;

    public MusicStateEvent(int duration){
        this.duration = duration;
    }

    public int getDuration(){
        return duration;
    }

}
