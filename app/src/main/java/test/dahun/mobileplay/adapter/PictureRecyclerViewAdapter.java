package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.viewholder.PictureViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class PictureRecyclerViewAdapter extends RecyclerView.Adapter<PictureViewHolder>{

    Context context;
    public PictureRecyclerViewAdapter(Context context){ this.context=context;}

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_picture,parent,false);
        return new PictureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        switch(position){
            case 0:
                Glide.with(context).load(R.drawable.photo_img_01).into(holder.imageView);
                break;
            case 1:
                Glide.with(context).load(R.drawable.photo_img_02).into(holder.imageView);
                break;
            case 2:
                Glide.with(context).load(R.drawable.photo_img_03).into(holder.imageView);
                break;
            case 3:
                Glide.with(context).load(R.drawable.photo_img_04).into(holder.imageView);
                break;
            case 4:
                Glide.with(context).load(R.drawable.photo_img_05).into(holder.imageView);
                break;
            case 5:
                Glide.with(context).load(R.drawable.photo_img_06).into(holder.imageView);
                break;
        }
    }

    @Override
    public int getItemCount() {
       return 6;
    }


}
