package test.dahun.mobileplay.tab;


import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.FanRecyclerviewAdapter;
import test.dahun.mobileplay.model.Fan;

import static test.dahun.mobileplay.R.drawable.comm_profileimg;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */


public class CommentFragment extends Fragment
{

    @BindView(R.id.navi) ImageButton navibtn;
    @BindView(R.id.mn_play) ImageButton playbtn;
    @BindView(R.id.mn_movie) ImageButton moviebtn;
    @BindView(R.id.mn_gallery) ImageButton galbtn;
    @BindView(R.id.mn_comm) ImageButton commbtn;

    @BindView(R.id.newwrite_btn) Button newwritebtn;
    @BindView(R.id.artist_date) TextView ardate;
    @BindView(R.id.artist_text) TextView artxt;
    @BindView(R.id.artist_name) TextView arname;
    @BindView(R.id.artist_img)
    CircleImageView arimg;

    final String TAG="CommentFragment";
    LinearLayout layout;

    public CommentFragment() {
        super();
    }


    //
    FanRecyclerviewAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    //
    View popupView;
    PopupWindow pw;

    private ArrayList<Fan> mItems = new ArrayList<>();
    Fan fan = new Fan();
    //

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_comment, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void initSetting() {
        ViewGroup.LayoutParams params = navibtn.getLayoutParams();
        params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
        params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        navibtn.requestLayout();
        navibtn.setImageResource(R.drawable.mn_default);
        navibtn.setTag(R.drawable.mn_default);
//navibutton
        navibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer)view.getTag() == R.drawable.mn_default){
                    playbtn.setVisibility(View.VISIBLE);
                    moviebtn.setVisibility(View.VISIBLE);
                    galbtn.setVisibility(View.VISIBLE);
                    commbtn.setVisibility(View.VISIBLE);

                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_click);
                    navibtn.setTag(R.drawable.mn_click);
                }else{
                    playbtn.setVisibility(View.GONE);
                    moviebtn.setVisibility(View.GONE);
                    galbtn.setVisibility(View.GONE);
                    commbtn.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                }
            }
        });

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "music", Toast.LENGTH_LONG).show();
            }
        });

        moviebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), " movie", Toast.LENGTH_LONG).show();

            }
        });

        galbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "gallery", Toast.LENGTH_LONG).show();

            }
        });

        commbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "community", Toast.LENGTH_LONG).show();

            }
        });
/////
        newwritebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ////new write popup
                popup(v);
                ////
            }
        });
        String img;
        String name="IU (아이유)";
        String txt="지금까지 함께해 주신 팬 여러분~~~\n" +
                "기다려 주셔서 감사해요. 올해는\n" +
                "콘서트를 할 수 있을 듯 해요 어쩌구\n" +
                "아이유의 현재를 담은 [CHAT-SHIRE]를\n" +
                "통해 당신은 당신의 미래-현재-과거의 \n" +
                "모습을 떠올리게 될것이다. 아이유\n" +
                "자신의 경험과 생각들이 바탕이 된 \n" +
                "이야기들이지만 우리 모두에게\n" +
                "더 자세한 얘기 바로 올릴게요";
        String date="2017.11.23 13:34";

        arname.setText(name);
        artxt.setText(txt);
        ardate.setText(date);

        arimg.setImageDrawable(getContext().getDrawable(comm_profileimg));
        arimg.setBackground(new ShapeDrawable(new OvalShape()));
        arimg.setClipToOutline(true);

        //List<Fan> items = new ArrayList<>();
        Fan[] item = new Fan[4];
        item[0] = new Fan("유애나시조","010 **** 3456","2017.08.22 21:20","앨범에 대한 감상 위주로 쓰게 하는게 맞을 거 같음. 공연정보 등 정 할얘기까 있으면 링크 정보만 담을 수 있게하는게 어떨까 싶기도 하고.");
        item[1] = new Fan("봉센세","010 **** 3456","2017.08.22 21:20","진짜진짜 명반인데..");
        item[2] = new Fan("ㅍㅌㅍㅋㅍㄹㅅ","010 **** 3456","2017.07.15 21:20","아이유 정주행중 가장 힘들었었던 앨범이 가장 좋다니 아이러니하다.");
        item[3] = new Fan("닉네임닉네임","010 **** 3456","2017.07.15 21:20","앨범에 대한 감상 위주로 쓰게 하는게 맞을 거 같음. 공연정보 등 정 할얘기까 있으면 링크 정보만 담을 수 있게하는게 어떨까 싶기도 하고.");
        // item[4] = new Fan("fan5","010-5555-2222","2017.11.08 3:46","e");

        for(int i=0;i<4;i++){
            mItems.add(item[i]);
        }

        //mItems.add(fan);
        adapter = new FanRecyclerviewAdapter(mItems);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adapter);

        //navibutton

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            Log.d("SetUserHint","Comment ON");
        else
            Log.d("SetUserHint","Comment OFF");
    }

    public void popup(View v){

        LayoutInflater inf = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView =inf.inflate(R.layout.plus_say,null);

        pw = new PopupWindow(v);

        pw.setContentView(popupView);
        pw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        // pw.setTouchable(true);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);

        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //popup 내용
        Button closebtn = (Button)popupView.findViewById(R.id.close_btn);
        Button writecancelbtn = (Button)popupView.findViewById(R.id.write_cancel_btn);
        final Button writebtn = (Button)popupView.findViewById(R.id.write_btn);
        final EditText writenick = (EditText)popupView.findViewById(R.id.write_nick);
        final EditText writetxt = (EditText)popupView.findViewById(R.id.write_text);

        writenick.requestFocus();




        /*InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(writenick, 0);
        */
        closebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                writenick.setText("");
                writetxt.setText("");
                pw.dismiss();
            }
        });

        writebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //서버에 저장되게 구현
                //일단 그냥 닫게 함
                writenick.setText("");
                writetxt.setText("");
                pw.dismiss();
            }
        });

        writecancelbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {writenick.clearComposingText();
                writenick.setText("");
                writetxt.setText("");
                pw.dismiss();
            }
        });

        pw.showAsDropDown(v);
    }



}