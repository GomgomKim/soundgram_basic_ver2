
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
 * Created by jeongdahun on 2017. 9. 11..
 */

public class CoverFragment extends Fragment
{
    final String TAG="CoverFragment";
    //@BindView(R.id.coverpager) VerticalViewPager coverPager;
    @BindView(R.id.sub_cover) FrameLayout cover;
    @BindView(R.id.ic_equalizerBtn) ImageView ic_equalizerBtn;
    LinearLayout layout = null;
    @BindView(R.id.maingBottomImage) ImageView mainBottomImage;
    @BindView(R.id.cover_top) ImageView mainTopImage;
    @BindView(R.id.cover_text) TextView maintext;
    boolean state= true;//커버


    public CoverFragment() {
        super();
    }


    @Nullable
    @SuppressLint("NewApi")
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.layout = (LinearLayout) inflater.inflate(R.layout.fragment_cover, container, false);
        ButterKnife.bind(this, this.layout);
        initSetting();
        return this.layout;
    }

    public void doWork(CallbackEvent callbackEvent){
        Log.i("GomgomKim", "doWork_cover");
        callbackEvent.callbackMethod();
    }

    public void initSetting() {
        Glide.with(getContext()).load(R.drawable.bg_main_bottom).into(mainBottomImage);
        Glide.with(getContext()).load(R.drawable.main_3_info).into(mainTopImage);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ic_equalizerBtn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);

        ic_equalizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(1);
            }
        });
        maintext.setText("한 떨기 스물셋 아이유의 사소한 현재. 그리고 당신의 지금.\n아이유 네 번재 미니앨범 [CHAT-SHIRE] 발매!\n\n모두가 기다려온 아이유의 네 번째 미니앨범\n[CHAT-SHIRE]가 드디어 공개됐다.\n\n지난 앨범들과 발표하는 자작곡들을 통해 섬세하면서 독특한 \n감성으로 수많은 팬들의 공감대를 불러일으키며\n뮤지션으로의 성장을 보여준 아이유는 이번 앨범을 통해\n프로듀싱까지 참여 영역을 확대해 앨범 전반에 자신의\n목소리를 빼곡하게 담아냈다.\n\n이번 앨범 [CHAT-SHIRE]는 23살의 아이유에게 일어나고\n보이는 일들과 사람들에게서 느낀 생각들을 소설 속 캐릭터에\n대입해 표현한 총 7곡이 수록되어 있으며, 앨범명\n[CHAT-SHIRE]는 각 곡의 캐릭터들이 살고 있는 주, 스물\n세 걸음이면 모두 돌아볼 수 있는 작은 사회를 의미한다.\n\n아이유의 현재를 담은 [CHAT-SHIRE]를 통해 당신은 당신의\n미래-현재-과거의 모습을 떠올리게 될것이다. 아이유 자신의\n경험과 생각들이 바탕이 된 이야기들이지만 우리 모두에게\n일어날 수 있고 생각할 수 있는 일들. 그 모습을\n[CHAT-SHIRE]안에서 함께 이야기하고 소통하길 원한다.");
        //coverPager.setAdapter(new CoverCustomPagerAdapter(getContext()));
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.sub_cover, new SubCoverFragment())
                .commit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ic_equalizerBtn);
                if(ApplicationStatus.isPlaying){
                    Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
                    return;
                }
                else{
                    Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);
                    return;
                }
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");


    }


}