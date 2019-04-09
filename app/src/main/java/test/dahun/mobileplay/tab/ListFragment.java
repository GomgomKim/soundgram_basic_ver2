package test.dahun.mobileplay.tab;


import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.PlayListAdapter;
import test.dahun.mobileplay.interfaces.ApplicationStatus;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    @BindView(R.id.like_gif) ImageView like_gif;

    @BindView(R.id.play_list) ListView play_list;

    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;

    RelativeLayout layout = null;
    PlayListAdapter playListAdapter;

    // 임시저장
    ArrayList<String> titles;
    ArrayList<Integer> heart_nums;

    //like animation
    private AnimationDrawable frameAnimation;

    public ListFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, layout);

        // 임시저장
        makeData();

        initSetting();
        btnSetting();
        return layout;
    }

    public void initSetting(){
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_off2).into(imageViewTarget);

        playListAdapter = new PlayListAdapter(getContext(), this);
        for(int i=0; i<titles.size(); i++){
            if ( i == 0 )
                playListAdapter.addItem(0, i+1, R.drawable.like_off, heart_nums.get(i), titles.get(i), "검정치마", 1);
            else
                playListAdapter.addItem(0, i+1, R.drawable.like_off, heart_nums.get(i), titles.get(i), "검정치마", 0);
        }

        // make content
        AssetManager am = getContext().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read=null;
        String content="";
        try {
            inputStream = am.open("content.txt");
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            br = new BufferedReader(inputStreamReader);

            while((read=br.readLine())!=null){
                content+=read;
                content+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        playListAdapter.addItem(1, "안녕하세요 '신현희와김루트' 입니다.", content);
        play_list.setAdapter(playListAdapter);
    }

    public void btnSetting(){
        home_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(0));
        list_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(1));
        play_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(2));
        gallery_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(3));
        sns_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(4));
    }

    public void viewGif(){
        like_gif.setBackgroundResource(R.drawable.like);
        like_gif.bringToFront();
        frameAnimation = (AnimationDrawable) like_gif.getBackground();
        if(frameAnimation.isRunning()) frameAnimation.stop();
        frameAnimation.start();
    }

    // 임시저장
    public void makeData(){
        titles = new ArrayList<>();
        heart_nums = new ArrayList<>();
        titles.add("신현희와김루트");      heart_nums.add(13);
        titles.add("오빠야");      heart_nums.add(1789);
        titles.add("Cap Song");       heart_nums.add(1142);
        titles.add("집 비던날");   heart_nums.add(486);
        titles.add("편한노래");           heart_nums.add(992);
        titles.add("날개");         heart_nums.add(96);
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
                    Glide.with(getContext()).load(R.drawable.mn_play_off2).into(imageViewTarget);
                    return;
                }
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }

}
