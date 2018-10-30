package com.testsite.reddittop.main;

import android.os.Bundle;

import com.testsite.reddittop.R;
import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.databinding.ActivityTopListBinding;
import com.testsite.reddittop.utils.OnPostClickListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class TopPostsActivity extends AppCompatActivity {

    private RedditViewModel postsViewModel;

    private ActivityTopListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_list);

        setSupportActionBar(binding.toolbar);

        postsViewModel = ViewModelProviders.of(this).get(RedditViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewmodel(postsViewModel);

        setupList();
    }

    private void setupList() {
        final TopPostsAdapter adapter = new TopPostsAdapter();
        adapter.setOnItemClickListener(new OnPostClickListener() {
            @Override
            public void onPostClicked(RedditPost post) {
                Timber.d(post.toString());
            }
        });
        binding.rvTop.setAdapter(adapter);
        binding.rvTop.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rvTop.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        postsViewModel.getPosts().observe(this, new Observer<PagedList<RedditPost>>() {
            @Override
            public void onChanged(PagedList<RedditPost> redditPosts) {
                adapter.submitList(redditPosts);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        postsViewModel.start();
    }
}
