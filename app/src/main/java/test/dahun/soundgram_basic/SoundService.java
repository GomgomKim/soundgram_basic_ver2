package test.dahun.soundgram_basic;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SoundService extends Service {
    public SoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean message = intent.getExtras().getBoolean("isPlay");
        if(message){

        } else{

        }

        return START_NOT_STICKY;
    }
}
