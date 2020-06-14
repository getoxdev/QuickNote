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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.quicknote.Utils.Constants.NOTE_ID_KEY;

public class EditorActivity extends AppCompatActivity {

    private EditorViewModel editorViewModel;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_done_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViewModel();
    }

    private void initViewModel()
    {
        editorViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);

        editorViewModel.liveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(NoteEntity noteEntity) {
                if(noteEntity!=null)
                {
                    noteEditText.setText(noteEntity.getText());
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            toolbarLayout.setTitle("New Note");
        }
        else
        {
            toolbarLayout.setTitle("Edit Note");
            int noteID = bundle.getInt(NOTE_ID_KEY);
            editorViewModel.loadNote(noteID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.home)
        {
            saveAndExit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAndExit();
    }

    private void saveAndExit()
    {
        editorViewModel.saveAndExit(noteEditText.getText().toString());
    }
}