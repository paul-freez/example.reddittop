package com.testsite.reddittop.main;

import android.os.Bundle;

import com.testsite.reddittop.R;
import com.testsite.reddittop.data.RedditPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

public class TopPostsActivity extends AppCompatActivity {

    private RedditViewModel postsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postsViewModel = ViewModelProviders.of(this).get(RedditViewModel.class);
        postsViewModel.getLoadingState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // TODO:
            }
        });
        postsViewModel.getPosts().observe(this, new Observer<PagedList<RedditPost>>() {
            @Override
            public void onChanged(PagedList<RedditPost> redditPosts) {
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
