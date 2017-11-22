package test.dahun.mobileplay.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import test.dahun.mobileplay.R;


/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public TextView mv_title;
    public VideoView mv_video;
    public TextView mv_exp;
    public Button mv_share_btn;
    public Button mv_play_btn;

    public MovieViewHolder(View itemView) {
        super(itemView);
        mv_title = (TextView)itemView.findViewById(R.id.movie_title);
        mv_video = (VideoView)itemView.findViewById(R.id.movie_video);
        mv_exp = (TextView)itemView.findViewById(R.id.movie_exp);
        mv_share_btn = (Button)itemView.findViewById(R.id.movie_share_btn);
        mv_play_btn = (Button)itemView.findViewById(R.id.movie_play_btn);
    }
}
