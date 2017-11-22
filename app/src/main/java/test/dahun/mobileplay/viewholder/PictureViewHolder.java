package test.dahun.mobileplay.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import test.dahun.mobileplay.R;


/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class PictureViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView ;

    public PictureViewHolder(View itemView) {
        super(itemView);
        imageView=(ImageView) itemView.findViewById(R.id.image);
    }
}
