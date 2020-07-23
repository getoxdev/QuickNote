package com.quicknote.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quicknote.Database.NoteEntity;
import com.quicknote.EditorActivity;
import com.quicknote.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.quicknote.Utils.Constants.NOTE_ID_KEY;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context mContext;
    private List<NoteEntity> mNotesList;

    public NotesAdapter(Context mContext, List<NoteEntity> mNotesList) {
        this.mContext = mContext;
        this.mNotesList = mNotesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.notes_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final NoteEntity noteEntity = mNotesList.get(position);
        holder.noteText.setText(noteEntity.getText());

        //animation for appearance of notes in fading fashion
        Animation cardAnim = AnimationUtils.loadAnimation(mContext,R.anim.fade_card);
        holder.noteCard.setAnimation(cardAnim);

        holder.noteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, EditorActivity.class);
                intent.putExtra(NOTE_ID_KEY,noteEntity.getID());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    public NoteEntity getNoteAtPosition(int position)
    {
        return mNotesList.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.note_text)
        TextView noteText;

        @BindView(R.id.noteCard)
        CardView noteCard;

<<<<<<< HEAD
=======


>>>>>>> d506508efd8fe58ae19483425f2bef971b80afea
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
