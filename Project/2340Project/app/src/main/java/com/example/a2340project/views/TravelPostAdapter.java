package com.example.a2340project.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import com.example.a2340project.R;
import com.example.a2340project.model.TravelPost;

public class TravelPostAdapter extends ListAdapter<TravelPost, TravelPostAdapter.ViewHolder> {

    public TravelPostAdapter() {
        super(new DiffUtil.ItemCallback<TravelPost>() {
            @Override
            public boolean areItemsTheSame(@NonNull TravelPost oldItem, @NonNull TravelPost newItem) {
                return oldItem.getTimestamp().equals(newItem.getTimestamp());
            }

            @Override
            public boolean areContentsTheSame(@NonNull TravelPost oldItem, @NonNull TravelPost newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelPost post = getItem(position);
        holder.tvDuration.setText(post.getDuration());
        holder.tvNotes.setText(post.getNotes());
    }

    static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView tvDuration, tvNotes;

        ViewHolder(View view) {
            super(view);
            tvDuration = view.findViewById(R.id.tv_duration);
            tvNotes = view.findViewById(R.id.tv_notes);
        }
    }
}
