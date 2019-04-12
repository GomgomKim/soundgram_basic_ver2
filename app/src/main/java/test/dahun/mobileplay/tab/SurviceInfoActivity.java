package test.dahun.mobileplay.tab;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;

public class SurviceInfoActivity extends AppCompatActivity {

    @BindView(R.id.top_layout) RelativeLayout top_layout;
    @BindView(R.id.btn_close) ImageButton btn_close;

    @BindView(R.id.bottom_layout) RelativeLayout bottom_layout;
    @BindView(R.id.logo_img) ImageView logo_img;

    @BindView(R.id.sns_btn) RelativeLayout sns_btn;
    @BindView(R.id.insta_btn) ImageView insta_btn;
    @BindView(R.id.facebook_btn) ImageView facebook_btn;
    @BindView(R.id.twt_btn) ImageView twt_btn;

    @BindView(R.id.util_btn) LinearLayout util_btn;
    @BindView(R.id.evaluation_btn) RelativeLayout evaluation_btn;
    @BindView(R.id.support_btn) RelativeLayout support_btn;
    @BindView(R.id.intro_btn) RelativeLayout intro_btn;
    @BindView(R.id.terms_btn) RelativeLayout terms_btn;

    View popupView;
    PopupWindow pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survice_info);
        ButterKnife.bind(this);
        iniSetting();
        snsSetting();
        utilSetting();
    }

    public void iniSetting(){
        Glide.with(this).load(R.drawable.logo_large)
                .apply(new RequestOptions().fitCenter()).into(logo_img);
    }

    public void snsSetting(){
        Glide.with(this).load(R.drawable.sns_isg)
                .apply(new RequestOptions().fitCenter().circleCrop()).into(insta_btn);
        Glide.with(this).load(R.drawable.sns_fb)
                .apply(new RequestOptions().fitCenter().circleCrop()).into(facebook_btn);
        Glide.with(this).load(R.drawable.sns_twt)
                .apply(new RequestOptions().fitCenter().circleCrop()).into(twt_btn);

        insta_btn.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/explore/tags/%EB%B9%84%ED%8B%80%EC%9B%8D%EC%8A%A4/?hl=ko"))));

        facebook_btn.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-kr.facebook.com/"))));

        twt_btn.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/?lang=ko"))));
    }

    public void utilSetting(){
        btn_close.setOnClickListener(view -> finish());

        evaluation_btn.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nhn.android.search"))));

        support_btn.setOnClickListener(view -> send_mail());

        intro_btn.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://soundgram.co.kr/"))));

        terms_btn.setOnClickListener(view -> terms_popup_window(view));
    }

    public void terms_popup_window(View v){

        LayoutInflater inf = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView =inf.inflate(R.layout.terms_of_service,null);

        pw = new PopupWindow(v);

        pw.setContentView(popupView);
        pw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        // pw.setTouchable(true);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);

        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //popup 내용
        ImageButton btn_close = (ImageButton)popupView.findViewById(R.id.btn_close);
        TextView main_text = (TextView)popupView.findViewById(R.id.main_text);

        // make content
        AssetManager am = getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read=null;
        String terms="";
        try {
            inputStream = am.open("intro.txt");
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            br = new BufferedReader(inputStreamReader);

            while((read=br.readLine())!=null){
                terms+=read;
                terms+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        main_text.setText(terms);

        pw.showAsDropDown(v);
    }

    public void send_mail(){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        // email setting 배열로 해놔서 복수 발송 가능
        String[] address = {"soundgram.info@soundgram.co.kr"};
        String user_os_version = Build.VERSION.RELEASE;

        String phone_name = Build.MODEL;
        email.putExtra(Intent.EXTRA_EMAIL, address);
        email.putExtra(Intent.EXTRA_SUBJECT,"[Soundgram 문의]");
        email.putExtra(Intent.EXTRA_TEXT, "어플리케이션 : 신현희와김루트\n"+
                "기기 종류 : "+phone_name+"\n"+
                "OS 버전 : "+user_os_version+"\n"+
                "앱 버전 : 1.3"+"\n"+
                "음반 정보 : 신현희와김루트\n");
        startActivity(email);
    }
}
