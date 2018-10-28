package com.testsite.reddittop.main;

import android.os.Bundle;

import com.testsite.reddittop.R;
import com.testsite.reddittop.data.Post;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class TopPostsActivity extends AppCompatActivity {

    private TopPostsViewModel postsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postsViewModel = ViewModelProviders.of(this).get(TopPostsViewModel.class);
        postsViewModel.getPostList().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                // TODO:
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        postsViewModel.start();
    }
}
