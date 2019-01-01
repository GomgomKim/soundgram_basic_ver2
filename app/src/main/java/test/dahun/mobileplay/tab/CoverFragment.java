
package test.dahun.mobileplay.tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.CoverCustomPagerAdapter;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.model.ApplicationStatus;
import test.dahun.mobileplay.ui.VerticalViewPager;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * Created by Gomgomkim on 2018. 12. 28..
 */

public class CoverFragment extends Fragment
{
    final String TAG="CoverFragment";

    @BindView(R.id.cover) LinearLayout cover;

    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;
    LinearLayout layout = null;

    //커버
    boolean state= false;
//    @BindView(R.id.sub_cover) FrameLayout sub_cover;


    public CoverFragment() {
        super();
    }


    @Nullable
    @SuppressLint("NewApi")
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_cover, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        btnSetting();
        resizeLayout();
        return layout;
    }

    public void initSetting() {
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(play_btn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);


        // 커버붙이기
        /*if(state){
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.sub_cover, new SubCoverFragment())
                    .commit();
        }*/
    }

    public void btnSetting(){
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(0);
            }
        });
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(1);
            }
        });
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(2);
            }
        });
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(3);
            }
        });
        sns_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(4);
            }
        });
    }

    public void resizeLayout(){
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float context_height = pxToDp(dm.heightPixels);
        final int layout_height = dpToPx(context_height - 442);
        cover.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams position = new LinearLayout.LayoutParams(
                        cover.getWidth(), layout_height
                );
                cover.setLayoutParams(position);
            }
        });

    }

    //dp를 px로
    public int dpToPx(float dp){
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return px;
    }

    //px를 dp로
    public float pxToDp(float px){
        float density = getContext().getResources().getDisplayMetrics().density;

        if(density == 1.0) density *= 4.0;
        else if(density == 1.5) density *= (8/3);
        else if(density == 2.0) density *= 2.0;

        float dp = px/density;
        return dp;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(play_btn);
                if(ApplicationStatus.isPlaying){
                    Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
                    return;
                }
                else{
                    Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);
                    return;
                }
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");


    }


}