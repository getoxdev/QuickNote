package com.quicknote;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quicknote.Database.NoteEntity;
import com.quicknote.Model.NotesAdapter;
import com.quicknote.ViewModels.ListViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.notes_list)
    RecyclerView notesRecyclerView;

    @BindView(R.id.fab_add_note)
    FloatingActionButton addNoteButton;

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.empty_text)
    TextView emptyText;

    @BindView(R.id.collapsed_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appbarlayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.search_edit_text)
    EditText searchInput;

    private List<NoteEntity> mNotesList = new ArrayList<>();
    private ListViewModel listViewModel;
    private NotesAdapter mNotesAdapter;
    private boolean isExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initViewModel();
        initRecyclerView();

        //changed typeface for collapsing toolbar layout
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsed_toolbar);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.myfont);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);


        //searchInput animation
        searchInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    appBarLayout.setExpanded(false,true);
                }
                else{
                    toolbar.setVisibility(View.VISIBLE);
                    appBarLayout.setExpanded(true,true);
                    appBarLayout.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0){
                    isExpanded = true;
                }

                else{
                    isExpanded = false;
                }
            }
        });


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
                    mNotesAdapter = new NotesAdapter(MainActivity.this, mNotesList);
                    notesRecyclerView.setAdapter(mNotesAdapter);
                    if(mNotesAdapter.getItemCount()==0)
                    {
                       emptyText.setVisibility(View.VISIBLE);
                    }
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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecyclerView.setLayoutManager(layoutManager);

        OverScrollDecoratorHelper.setUpOverScroll(notesRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
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

    private void deleteAllData()
    {
        listViewModel.deleteAllData();
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
            case R.id.action_delete_all_notes:{
                deleteAllData();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private int counter = 0;

    @Override
    public void onBackPressed() {


        if(!isExpanded){

            if(counter == 0){
                appBarLayout.setExpanded(true, true);
                appBarLayout.setVisibility(View.VISIBLE);
                searchInput.clearFocus();
                counter++;

            }
            else if(counter == 1){

                super.onBackPressed();

            }
        }
        else
        {
            super.onBackPressed();
        }
    }
}