package com.example.a2340project.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a2340project.R;

import java.util.List;

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.CollaboratorViewHolder> {

    private List<String> collaboratorsList; // List of collaborator names
    private Context context;

    public CollaboratorAdapter(Context context, List<String> collaboratorsList) {
        this.context = context;
        this.collaboratorsList = collaboratorsList;
    }

    @Override
    public CollaboratorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_collaborator_item, parent, false);
        return new CollaboratorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollaboratorViewHolder holder, int position) {
        String collaborator = collaboratorsList.get(position);
        holder.nameTextView.setText(collaborator);
        // Set the avatar image here if available
    }

    @Override
    public int getItemCount() {
        return collaboratorsList.size();
    }

    public static class CollaboratorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView avatarImageView;

        public CollaboratorViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.collaboratorName);
            avatarImageView = itemView.findViewById(R.id.collaboratorAvatar);
        }
    }
}
