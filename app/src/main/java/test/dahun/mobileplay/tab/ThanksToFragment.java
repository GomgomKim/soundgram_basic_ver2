package test.dahun.mobileplay.tab;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThanksToFragment extends Fragment {

    RelativeLayout layout = null;

    @BindView(R.id.singer_img) ImageView singer_img;

    @BindView(R.id.intro_list) ListView intro_list;

    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;

    ThanksToAdapter thanksToAdapter;

    public ThanksToFragment() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_thanks_to, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        //resizeLayout();
        btnSetting();
        return layout;
    }

    public void initSetting(){
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);


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

        // 리스트 생성
        thanksToAdapter = new ThanksToAdapter(getContext());
        thanksToAdapter.addItem("Thanks to FAN", "from 검정치마", intro);
        intro_list.setAdapter(thanksToAdapter);

        // 이미지 원형으로 만들기
        Glide.with(getContext()).load(R.drawable.sns_mainimg)
                                .apply(new RequestOptions().circleCrop())
                                .into(singer_img);
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
