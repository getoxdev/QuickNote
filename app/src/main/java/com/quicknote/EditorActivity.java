package com.quicknote;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.quicknote.Database.NoteEntity;
import com.quicknote.Utils.Constants;
import com.quicknote.ViewModels.EditorViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.quicknote.Utils.Constants.EDITING_KEY;
import static com.quicknote.Utils.Constants.NOTE_ID_KEY;

public class EditorActivity extends AppCompatActivity {

    private EditorViewModel editorViewModel;
    private Boolean newNote;
    private Boolean isEditing = false;

    @BindView(R.id.note_edit_text)
    EditText noteEditText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        if(savedInstanceState!=null)
        {
            isEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_done_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViewModel();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putBoolean(EDITING_KEY,true);
        super.onSaveInstanceState(outState);
    }

    private void initViewModel()
    {
        editorViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);

        editorViewModel.liveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(NoteEntity noteEntity) {
                if(noteEntity!=null && !isEditing)
                {
                    noteEditText.setText(noteEntity.getText());
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            toolbarLayout.setTitle("New Note");
            newNote = true;
        }
        else
        {
            toolbarLayout.setTitle("Edit Note");
            int noteID = bundle.getInt(NOTE_ID_KEY);
            editorViewModel.loadNote(noteID);
            newNote = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(!newNote)
        {
            getMenuInflater().inflate(R.menu.menu_editor,menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            saveAndExit();
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.action_delete)
        {
            deleteNote();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAndExit();
    }

    private void deleteNote()
    {
        editorViewModel.deleteNote();
    }

    private void saveAndExit()
    {
        editorViewModel.saveAndExit(noteEditText.getText().toString());
    }
}