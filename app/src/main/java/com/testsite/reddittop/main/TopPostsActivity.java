package com.testsite.reddittop.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.testsite.reddittop.R;
import com.testsite.reddittop.data.posts.model.RedditPostDTO;
import com.testsite.reddittop.databinding.ActivityTopListBinding;
import com.testsite.reddittop.utils.ChromeTabsIntent;
import com.testsite.reddittop.utils.OnPostClickListener;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;
import com.testsite.reddittop.utils.exceptions.UnauthorizedException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import saschpe.android.customtabs.CustomTabsHelper;
import timber.log.Timber;

@AndroidEntryPoint
public class TopPostsActivity extends AppCompatActivity {

    @Inject
    CustomTabsHelper customTabsHelper;

    private RedditViewModel postsViewModel;

    private ActivityTopListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_list);

        setSupportActionBar(binding.toolbar);

        postsViewModel = new ViewModelProvider(this).get(RedditViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewmodel(postsViewModel);

        setupList();

        // Register for Chrome Tabs warmup
        getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                customTabsHelper.bindCustomTabsService(TopPostsActivity.this);
            }

            @Override
            public void onPause(@NonNull LifecycleOwner owner) {
                customTabsHelper.unbindCustomTabsService(TopPostsActivity.this);
            }
        });

        postsViewModel.getErrorHandler().observe(this, new Observer<ErrorHandler>() {
            @Override
            public void onChanged(ErrorHandler errorHandler) {
                if (errorHandler.getException() instanceof UnauthorizedException) {
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
            public void onPostClicked(RedditPostDTO post) {
                postsViewModel.openPost(post);
            }
        });
        binding.setAdapter(adapter);

        postsViewModel.getPosts().observe(this, new Observer<PagedList<RedditPostDTO>>() {
            @Override
            public void onChanged(PagedList<RedditPostDTO> redditPostDTOS) {
                adapter.submitList(redditPostDTOS);
            }
        });
        postsViewModel.getExternalIntent().observe(this, new Observer<ChromeTabsIntent<RedditPostDTO>>() {
            @Override
            public void onChanged(ChromeTabsIntent<RedditPostDTO> redditPostChromTabsIntent) {
                CustomTabsHelper.Companion.openCustomTab(TopPostsActivity.this, redditPostChromTabsIntent.getIntent(),
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
                            }
                        });
            }
        });

        binding.srlRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.srlRefresh.setRefreshing(true);

                postsViewModel.loadPosts();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        postsViewModel.start();
    }
}
