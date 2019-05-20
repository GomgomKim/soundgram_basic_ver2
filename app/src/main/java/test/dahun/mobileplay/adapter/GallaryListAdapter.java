package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.interfaces.ServiceStateInterface;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.tab.GallaryFragment;
import test.dahun.mobileplay.tab.ImageActivity;
import test.dahun.mobileplay.tab.VideoActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class GallaryListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;

    private ArrayList<GallaryListItem> mItems = new ArrayList<>();

    GallaryListItem current_item;


    public GallaryListAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public GallaryListItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 뷰 종류
    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    // 뷰 여러개 붙이기
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        current_item = getItem(position);
        int res = 0;

        if(convertView == null){
            res = getItemViewType(position);
            switch (res){
                case 0:
                    convertView = inflater.inflate(R.layout.gallary_img, parent, false);
                    ViewHolderImg viewHolderImg = new ViewHolderImg();
                    viewHolderImg.gallary_img_layout = (RelativeLayout) convertView.findViewById(R.id.gallary_img_layout);
                    viewHolderImg.gallary_img = (ImageView) convertView.findViewById(R.id.gallary_img);
                    viewHolderImg.gallary_img_title = (TextView) convertView.findViewById(R.id.gallary_img_title);

                    Picasso.with(context).load(current_item.getImg_resource()).into(viewHolderImg.gallary_img);
                    viewHolderImg.gallary_img.bringToFront();
                    viewHolderImg.gallary_img_title.setText(current_item.getTitle());


                    final String image_title = current_item.getTitle();
                    final int img_resource = current_item.getImg_resource();

                    viewHolderImg.gallary_img_layout.setOnClickListener(view -> {
                        Intent intent = new Intent(context, ImageActivity.class);
                        intent.putExtra("image_title", image_title);
                        intent.putExtra("img_resource", img_resource);
                        context.startActivity(intent);
                    });

                    break;
                case 1:
                    convertView = inflater.inflate(R.layout.gallary_video, parent, false);
                    ViewHolderVideo viewHolderVideo = new ViewHolderVideo();
                    viewHolderVideo.gallary_video_img = (ImageView) convertView.findViewById(R.id.gallary_video_img);
                    viewHolderVideo.gallary_video_play = (ImageButton) convertView.findViewById(R.id.gallary_video_play);
                    viewHolderVideo.gallary_video_title = (TextView) convertView.findViewById(R.id.gallary_video_title);
                    Picasso.with(context).load(current_item.getImg_resource()).into(viewHolderVideo.gallary_video_img);
//                gallary_video_img.setImageResource(current_item.getImg_resource());
                /*String url = "https://img.youtube.com/vi/"+current_item.getVideo_code()+"/0.jpg";
                Glide.with(context).load(url)
                        .apply(new RequestOptions().fitCenter()).into(gallary_video_img);*/
                    final String video_id = current_item.getVideo_code();
                    final String video_title = current_item.getTitle();


                    // play 버튼
                    viewHolderVideo.gallary_video_play.setOnClickListener(view -> {

                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if(networkInfo != null && networkInfo.isConnected()){

                            Intent intent = new Intent(context, VideoActivity.class);
                            intent.putExtra("video_id", video_id);
                            intent.putExtra("video_name", video_title);
                            intent.putExtra("music_index", ((ServiceStateInterface)context).getServiceState().getIndex());
                            context.startActivity(intent);

                        } else{
                            // alert
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
                            alert.setMessage("네트워크가 연결되지 않았습니다. Wi-Fi 또는 데이터를 활성화 해주세요.");
                            alert.show();
                        }
                    });
                    viewHolderVideo.gallary_video_title.setText(video_title);

                    break;
            }

        }
        return convertView;
    }


    public void addItem(int type, String title, int img_resource) {
        GallaryListItem mItem = new GallaryListItem();

        mItem.setType(type);
        mItem.setTitle(title);
        mItem.setImg_resource(img_resource);

        mItems.add(mItem);

    }

    public void addItem(int type, String title, int img_resource, String video_code){
        GallaryListItem mItem = new GallaryListItem();

        mItem.setType(type);
        mItem.setTitle(title);
        mItem.setImg_resource(img_resource);
        mItem.setVideo_code(video_code);

        mItems.add(mItem);
    }

    static class ViewHolderImg{
        RelativeLayout gallary_img_layout;
        ImageView gallary_img;
        TextView gallary_img_title;
    }

    static class ViewHolderVideo{
        ImageView gallary_video_img;
        ImageButton gallary_video_play;
        TextView gallary_video_title;
    }



}
