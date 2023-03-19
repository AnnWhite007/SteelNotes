package com.babintseva.steelnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.babintseva.steelnotes.adapter.Note;
import com.babintseva.steelnotes.db.NoteConstants;
import com.babintseva.steelnotes.db.NoteDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity {
    private EditText edHeader, edExp;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> launcher;
    private String noteImUri = "empty";
    private boolean isEditState = true;
    private ConstraintLayout imageContainer;
    private FloatingActionButton addImageButton;
    private ImageButton editImage;
    private NoteDbManager noteDbManager;
    private Note note;
    private Animation animAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getIntents();
    }

    private void init() {
        edHeader = findViewById(R.id.editHeader);
        imageContainer = findViewById(R.id.imageContainer);
        imageView = findViewById(R.id.imageView);
        edExp = findViewById(R.id.editExp);
        addImageButton = findViewById(R.id.addImageButton);
        editImage = findViewById(R.id.edit_image_button);
        noteDbManager = new NoteDbManager(this);
        noteDbManager.openDb();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            noteImUri = result.getData().getData().toString();
                            imageView.setImageURI(result.getData().getData());
                            getContentResolver().takePersistableUriPermission(result.getData().getData()
                                    , Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                });
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
    }

    private void getIntents() {
        Intent intent = getIntent();
        if (intent != null) {
            note = (Note) intent.getSerializableExtra(NoteConstants.NOTE_INTENT);
            isEditState = intent.getBooleanExtra(NoteConstants.EDIT_STATE, true);
            if (!isEditState) {

                edHeader.setText(note.getHeader());
                edExp.setText(note.getExp());
                if (!note.getUri().equals("empty")) {
                    noteImUri = note.getUri();
                    imageContainer.setVisibility(View.VISIBLE);
                    addImageButton.setVisibility(View.GONE);
                    imageView.setImageURI(Uri.parse(note.getUri()));
                }
            }
        }
    }

    public void onClickSave(View view) {
        view.startAnimation(animAlpha);
        String header = edHeader.getText().toString();
        String exp = edExp.getText().toString();

        if (header.equals("") && exp.equals("")) {
            Toast.makeText(this, R.string.unsaved, Toast.LENGTH_SHORT).show();

        } else {
            if (header.equals("")) {
                header = exp.length() < 20 ? exp + "..." : exp.substring(0, 20) + "...";
            }
            if (isEditState) {
                noteDbManager.insertToDb(header, noteImUri, exp);
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

            } else {
                noteDbManager.updateItemInDb(note.getId(), header, noteImUri, exp);
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }
        }
        noteDbManager.closeDb();
        finish();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }

    public void onClickAddImage(View view) {
        view.startAnimation(animAlpha);
        imageContainer.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void onClickEditImage(View view) {
        view.startAnimation(animAlpha);
        Intent editor = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        editor.setType("image/*");
        launcher.launch(editor);
    }

    public void onClickDeleteImage(View view) {
        view.startAnimation(animAlpha);
        imageView.setImageResource(R.drawable.ic_defoult_image);
        noteImUri = "empty";
        imageContainer.setVisibility(View.GONE);
        addImageButton.setVisibility(View.VISIBLE);
    }

}