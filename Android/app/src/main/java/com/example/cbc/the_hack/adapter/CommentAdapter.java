package com.example.cbc.the_hack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cbc.the_hack.R;
import com.example.cbc.the_hack.model.Comment;

public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.comment, parent, false);
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;


    }
}
