package test.dahun.mobileplay.events;

public class FinishMusicEvent {
    public int state;

    public FinishMusicEvent(int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }

}
