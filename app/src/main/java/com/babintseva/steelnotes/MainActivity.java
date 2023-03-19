package com.babintseva.steelnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.babintseva.steelnotes.adapter.NotesAdapter;
import com.babintseva.steelnotes.db.NoteDbManager;

public class MainActivity extends AppCompatActivity {

    private NoteDbManager noteDbManager;
    private RecyclerView noteListView;
    private NotesAdapter notesAdapter;
    private Animation animAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        noteDbManager = new NoteDbManager(this);
        noteListView = findViewById(R.id.noteListView);
        notesAdapter = new NotesAdapter(this);
        noteListView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(noteListView);
        noteListView.setAdapter(notesAdapter);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

    }

    @Override
    protected void onResume() {
        super.onResume();

        noteDbManager.openDb();
        notesAdapter.upDateAdapter(noteDbManager.readFromDb());
    }

    public void onClickAdd(View view) {
        view.startAnimation(animAlpha);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteDbManager.closeDb();
    }


    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                notesAdapter.removeItem(viewHolder.getAdapterPosition(), noteDbManager);
            }
        });
    }
}