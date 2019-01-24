
package test.dahun.mobileplay.tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import test.dahun.mobileplay.model.ApplicationStatus;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * Created by Gomgomkim on 2018. 12. 28..
 */

public class CoverFragment extends Fragment
{
    final String TAG="CoverFragment";

    @BindView(R.id.cover) LinearLayout cover;

    @BindView(R.id.cover_img) ImageView cover_img;
    @BindView(R.id.reflect_img) ImageView reflect_img;

    @BindView(R.id.info_btn) ImageButton info_btn;

    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;

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
        btnSetting();
        return layout;
    }

    public void initSetting() {
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);

        Glide.with(getContext()).load(R.drawable.albumimg_01)
                .apply(new RequestOptions().fitCenter()).into(cover_img);

        Glide.with(getContext()).load(R.drawable.album_reflectlight)
                .apply(new RequestOptions().fitCenter()).into(reflect_img);

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SurviceInfoActivity.class));
            }
        });

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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
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