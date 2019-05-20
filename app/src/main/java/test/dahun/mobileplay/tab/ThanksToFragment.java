package test.dahun.mobileplay.tab;


import android.content.res.AssetManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.ThanksToAdapter;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.interfaces.ButtonInterface;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThanksToFragment extends Fragment {

    RelativeLayout layout = null;

    @BindView(R.id.singer_img) ImageView singer_img;

    @BindView(R.id.intro_list) ListView intro_list;

    @BindView(R.id.middle_bg) ImageView middle_bg;

    ThanksToAdapter thanksToAdapter;

    public ThanksToFragment() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_thanks_to, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
//        setAni();
        return layout;
    }

    public void setAni() {
        Animation animTransRight = AnimationUtils
                .loadAnimation(getContext(), R.anim.cover_ani);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            playAni(animTransRight);
        }, 4000);
    }

    public void playAni(Animation animTransRight){
        singer_img.startAnimation(animTransRight);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            playAni(animTransRight);
        }, 26000);
    }

    public void initSetting(){
        // make content
        AssetManager am = getContext().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read=null;
        String intro="";
        try {
            inputStream = am.open("intro.txt");
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            br = new BufferedReader(inputStreamReader);

            while((read=br.readLine())!=null){
                intro+=read;
                intro+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(getContext()).load(R.drawable.bg_thanks).into(middle_bg);

        // 리스트 생성
        thanksToAdapter = new ThanksToAdapter(getContext());
        thanksToAdapter.addItem("기똥찬 오리엔탈 명랑 어쿠스틱 듀오 ‘신현희와김루트’ 다양한 분위기로 대중들을 사로잡는다.", "", intro);
        intro_list.setAdapter(thanksToAdapter);

        // 이미지 원형으로 만들기
        Glide.with(getContext()).load(R.drawable.album_story_profile)
                                .apply(new RequestOptions().circleCrop())
                                .into(singer_img);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                ((ButtonInterface)getContext()).reset();
                ((ButtonInterface)getContext()).snsOn();
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }

}
