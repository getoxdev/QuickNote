package com.quicknote;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quicknote.Database.NoteEntity;
import com.quicknote.Model.NotesAdapter;
import com.quicknote.Utils.SampleData;
import com.quicknote.ViewModels.ListViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.notes_list)
    RecyclerView notesRecyclerView;

    @BindView(R.id.fab_add_note)
    FloatingActionButton addNoteButton;

    private List<NoteEntity> mNotesList = new ArrayList<>();
    private ListViewModel listViewModel;
    NotesAdapter mNotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewModel();

        initRecyclerView();
    }

    @OnClick(R.id.fab_add_note)
    void createNewNote()
    {
        Intent intent = new Intent(MainActivity.this,EditorActivity.class);
        startActivity(intent);
    }

    private void initViewModel()
    {
        Observer<List<NoteEntity>> notesObserver = new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                mNotesList.clear();
                mNotesList.addAll(noteEntities);

                if(mNotesAdapter == null)
                {
                    mNotesAdapter = new NotesAdapter(MainActivity.this,mNotesList);
                    notesRecyclerView.setAdapter(mNotesAdapter);
                }
                else
                {
                    mNotesAdapter.notifyDataSetChanged();
                }
            }
        };

        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        listViewModel.mNotesList.observe(MainActivity.this,notesObserver);
    }

    private void initRecyclerView()
    {
        notesRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteNote(mNotesAdapter.getNoteAtPosition(viewHolder.getAdapterPosition()));
            }
        });

        itemTouchHelper.attachToRecyclerView(notesRecyclerView);
    }

    private void deleteNote(NoteEntity noteEntity) {
        listViewModel.deleteNote(noteEntity);
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {
            case R.id.add_sample_data:{
                addSampleData();
                return true;
            }

            case R.id.delete_all_data:{
                deleteAllData();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addSampleData()
    {
        listViewModel.addSampleData();
    }

    private void deleteAllData()
    {
        listViewModel.deleteAllData();
    }
}