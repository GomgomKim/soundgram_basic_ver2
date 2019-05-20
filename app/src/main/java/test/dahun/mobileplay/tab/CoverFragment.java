
package test.dahun.mobileplay.tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.interfaces.ButtonInterface;
import test.dahun.mobileplay.main.MainActivity;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * Created by Gomgomkim on 2018. 12. 28..
 */

public class CoverFragment extends Fragment
{
    @BindView(R.id.cover) LinearLayout cover;

    @BindView(R.id.cover_img) ImageView cover_img;
    @BindView(R.id.reflect_img) ImageView reflect_img;
    @BindView(R.id.cover_bottom) ImageView cover_bottom;
    @BindView(R.id.bottom_layout) RelativeLayout bottom_layout;

    @BindView(R.id.info_btn_back) RelativeLayout info_btn_back;
    @BindView(R.id.info_btn_tint) ImageView info_btn_tint;
    @BindView(R.id.info_btn) ImageView info_btn;

    RelativeLayout layout = null;

    public CoverFragment() {
        super();
    }


    @Nullable
    @SuppressLint("NewApi")
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_cover, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        setAni();
        return layout;
    }

    public void initSetting() {
        Glide.with(getContext()).load(R.drawable.cover)
                .apply(new RequestOptions().fitCenter()).into(cover_img);

        Glide.with(getContext()).load(R.drawable.bg_home)
                .apply(new RequestOptions().fitCenter()).into(cover_bottom);

        cover.bringToFront();

        Glide.with(getContext()).load(R.drawable.album_reflectlight)
                .apply(new RequestOptions().fitCenter()).into(reflect_img);

        Glide.with(getContext()).load(R.drawable.cover_back)
                .apply(new RequestOptions().circleCrop()).into(info_btn_tint);

        Glide.with(getContext()).load(R.drawable.info_btn)
                .apply(new RequestOptions().circleCrop()).into(info_btn);

        info_btn_back.setOnClickListener(view -> startActivity(new Intent(getActivity(), SurviceInfoActivity.class)));
    }

    public void setAni() {
        Animation animTransRight = AnimationUtils.loadAnimation(getContext(), R.anim.cover_ani);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            playAni(animTransRight);
            bottom_layout.bringToFront();
        }, 4000);
    }

    public void playAni(Animation animTransRight){
        cover_img.startAnimation(animTransRight);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            playAni(animTransRight);
            bottom_layout.bringToFront();
        }, 26000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                ((ButtonInterface)getContext()).reset();
                ((ButtonInterface)getContext()).homeOn();
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }


}