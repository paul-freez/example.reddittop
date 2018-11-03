package com.testsite.reddittop.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.testsite.reddittop.R;
import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.databinding.ActivityTopListBinding;
import com.testsite.reddittop.utils.CustomTabsInstance;
import com.testsite.reddittop.utils.OnPostClickListener;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import saschpe.android.customtabs.CustomTabsHelper;
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

        // Register for Chrome Tabs warmup
        getLifecycle().addObserver(new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                switch (event) {
                    case ON_RESUME:
                        CustomTabsInstance.getInstance().bindCustomTabsService(TopPostsActivity.this);
                        break;
                    case ON_PAUSE:
                        CustomTabsInstance.getInstance().unbindCustomTabsService(TopPostsActivity.this);
                        break;
                }
            }
        });

        postsViewModel.getErrorHandler().observe(this, new Observer<ErrorHandler>() {
            @Override
            public void onChanged(ErrorHandler errorHandler) {
                if (errorHandler.getException() instanceof ErrorHandler.UnauthorizedException){
                    // Try re-authorize
                    Timber.d("%s. Retrying...", errorHandler.getMessage());
                    postsViewModel.authorize();

                } else {
                    Snackbar.make(binding.getRoot(), errorHandler.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupList() {
        final TopPostsAdapter adapter = new TopPostsAdapter();
        adapter.setOnItemClickListener(new OnPostClickListener() {
            @Override
            public void onPostClicked(RedditPost post) {
                postsViewModel.openPost(post);
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
        postsViewModel.getExternalIntent().observe(this, new Observer<CustomTabsInstance.ChromTabsIntent<RedditPost>>() {
            @Override
            public void onChanged(CustomTabsInstance.ChromTabsIntent<RedditPost> redditPostChromTabsIntent) {
                CustomTabsHelper.openCustomTab(TopPostsActivity.this, redditPostChromTabsIntent.getIntent(),
                        Uri.parse(redditPostChromTabsIntent.getContent().getLink()),
                        new CustomTabsHelper.CustomTabFallback() {
                            @Override
                            public void openUri(Context context, Uri uri) {
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                                String title = getResources().getString(R.string.chooser_title);
                                // Create intent to show chooser
                                Intent chooser = Intent.createChooser(viewIntent, title);

                                // Verify the intent will resolve to at least one activity
                                if (viewIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(chooser);
                                }
                                // TODO: Make sure this will work when Chrome is not installed, but Reddit app IS ->
                                // TODO: open in Reddit
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        postsViewModel.start();
    }
}
