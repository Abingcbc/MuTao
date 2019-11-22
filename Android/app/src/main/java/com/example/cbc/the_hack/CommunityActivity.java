package com.example.cbc.the_hack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.etsy.android.grid.StaggeredGridView;
import com.example.cbc.the_hack.adapter.CommentAdapter;
import com.example.cbc.the_hack.model.Comment;

public class CommunityActivity extends AppCompatActivity {

    private CommentAdapter adapter;
    private StaggeredGridView staggeredGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        staggeredGridView = findViewById(R.id.grid_view);
        adapter = new CommentAdapter(this, R.id.comment_text);
        for (int i = 0; i < 10; i++) {
            adapter.add(new Comment());
        }
        staggeredGridView.setAdapter(adapter);
    }
}
