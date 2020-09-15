package com.quicknote;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.platform.MaterialArcMotion;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.quicknote.Database.NoteEntity;
import com.quicknote.Utils.Constants;
import com.quicknote.ViewModels.EditorViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
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

    @BindView(R.id.toolbar_editor)
    Toolbar toolbar;

    @BindView(R.id.title_of_note)
    EditText noteTitle;


    @BindView(R.id.container_transform)
    ConstraintLayout coordinatorLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_editor);


        ButterKnife.bind(this);

        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setPathMotion(new MaterialArcMotion());
        transform.addTarget(coordinatorLayout);
        transform.setDuration(400);

        getWindow().setSharedElementEnterTransition(transform);



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
                    noteTitle.setText(noteEntity.getTitle());
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            getSupportActionBar().setTitle("New Note");
            newNote = true;
        }
        else
        {
            getSupportActionBar().setTitle("Edit Note");
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
            finishAfterTransition();
            return true;
        }
        else if(item.getItemId() == R.id.action_delete)
        {
            deleteNote();
            finishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAndExit();
        finishAfterTransition();
    }

    private void deleteNote()
    {
        editorViewModel.deleteNote();
    }

    private void saveAndExit()
    {
        editorViewModel.saveAndExit(noteEditText.getText().toString(),noteTitle.getText().toString());
    }
}