package com.example.cbc.the_hack.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.example.cbc.the_hack.R;
import com.example.cbc.the_hack.model.Comment;

import java.util.ArrayList;
import java.util.Random;

public class CommentAdapter extends ArrayAdapter<String> {

    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final LayoutInflater mLayoutInflater;
    private int collect;//0 uncollect, 1 collect

    public CommentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        collect = 0;
    }

    class ViewHolder {
        DynamicHeightTextView txtLineOne;
        DynamicHeightImageView image;
        ImageButton btnGo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = convertView.findViewById(R.id.txt_line1);
            vh.btnGo = convertView.findViewById(R.id.btn_go);
            vh.image = convertView.findViewById(R.id.img_dht);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);

        Log.d("muta", "getView position:" + position + " h:" + positionHeight);
        int resId;
        BitmapFactory.Options options = new BitmapFactory.Options();
        double height;
        double width;
        double rate;
        String image_name;
        image_name = "item_"+position;
        resId = getContext().getResources().getIdentifier(image_name, "drawable",
                getContext().getPackageName());
        vh.image.setBackgroundResource(resId);
        BitmapFactory.decodeResource(getContext().getResources(),resId,options);
        height = options.outHeight;
        width = options.outWidth;
        rate = height/width;
        vh.image.setHeightRatio(rate);
        AssetManager mgr = getContext().getAssets();
        Typeface tf = Typeface.createFromAsset(mgr,"fonts/STXINGKA.TTF");
        vh.txtLineOne.setTypeface(tf);
        vh.txtLineOne.setText(getItem(position));

        vh.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(collect==0){
                    vh.btnGo.setBackgroundResource(R.drawable.star_full2);
                    Toast.makeText(getContext(), "已收藏！" , Toast.LENGTH_SHORT).show();
                    collect=1;
                }else if (collect==1){
                    vh.btnGo.setBackgroundResource(R.drawable.star2);
                    Toast.makeText(getContext(), "已取消收藏！" , Toast.LENGTH_SHORT).show();
                    collect=0;
                }
            }
        });

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d("mutao", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}
