package com.babintseva.steelnotes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babintseva.steelnotes.EditActivity;
import com.babintseva.steelnotes.R;
import com.babintseva.steelnotes.db.NoteConstants;
import com.babintseva.steelnotes.db.NoteDbManager;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context context;
    private List<Note> notesList;

    public NotesAdapter(Context context) {
        this.context = context;
        notesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        return new NotesViewHolder(view, context, notesList);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.setData(notesList.get(position).getHeader());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvItemOfNote;
        private Context context;
        private List<Note> notesList;

        public NotesViewHolder(@NonNull View itemView, Context context, List<Note> notesList) {
            super(itemView);
            this.context = context;
            this.notesList = notesList;
            tvItemOfNote = itemView.findViewById(R.id.tvItemOfNote);
            itemView.setOnClickListener(this);
        }

        public void setData(String header) {
            tvItemOfNote.setText(header);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra(NoteConstants.NOTE_INTENT, notesList.get(getAdapterPosition()));
            intent.putExtra(NoteConstants.EDIT_STATE, false);
            context.startActivity(intent);
        }
    }

    public void upDateAdapter(List<Note> newList) {
        notesList.clear();
        notesList.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int pos, NoteDbManager noteDbManager) {
        noteDbManager.deleteItemFromDb(notesList.get(pos).getId());
        notesList.remove(pos);
        notifyItemRangeChanged(0, notesList.size());
        notifyItemRemoved(pos);
    }
}
