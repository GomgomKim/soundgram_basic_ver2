package test.dahun.mobileplay.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import test.dahun.mobileplay.R;


public class SplashActivity extends Activity {

    private ImageView loading;
    private AnimationDrawable frameAnimation;
    int state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewGif();
        startLoading();
    }

    public void viewGif(){
        loading = (ImageView)findViewById(R.id.loading_gif);
        loading.setBackgroundResource(R.drawable.loading);
        frameAnimation = (AnimationDrawable) loading.getBackground();
        frameAnimation.start();
    }

    public void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);

    }

}
