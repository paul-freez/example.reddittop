package com.testsite.reddittop.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.databinding.ItemRedditPostBinding;
import com.testsite.reddittop.utils.OnPostClickListener;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by paulf
 */
public class TopPostsAdapter extends PagedListAdapter<RedditPost, TopPostsAdapter.PostViewHolder> {

    private OnPostClickListener listener;

    private static final DiffUtil.ItemCallback<RedditPost> DIFF_CALLBACK = new DiffUtil.ItemCallback<RedditPost>() {
        @Override
        public boolean areItemsTheSame(@NonNull RedditPost oldItem, @NonNull RedditPost newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RedditPost oldItem, @NonNull RedditPost newItem) {
            return oldItem == newItem;
        }
    };

    protected TopPostsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRedditPostBinding binding = ItemRedditPostBinding.inflate(inflater, parent, false);

        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.binding.setPost(getItem(position));
        holder.binding.setListener(listener);
    }

    public void setOnItemClickListener(OnPostClickListener listener) {
        this.listener = listener;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {

        private final ItemRedditPostBinding binding;

        PostViewHolder(ItemRedditPostBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
