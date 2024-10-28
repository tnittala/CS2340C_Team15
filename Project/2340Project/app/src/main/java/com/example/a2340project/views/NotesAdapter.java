package com.example.a2340project.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2340project.R;
import com.example.a2340project.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void setNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteContent.setText(note.getContent());

        // Format the timestamp into a readable date format
        String formattedDate = new SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.getDefault()).format(new Date(note.getTimestamp()));
        holder.noteTimestamp.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteContent;
        TextView noteTimestamp;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteContent = itemView.findViewById(R.id.noteContent);
            noteTimestamp = itemView.findViewById(R.id.noteTimestamp);
        }
    }
}
