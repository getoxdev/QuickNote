package com.quicknote.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        //@BindView(R.id.fab_edit)
        //FloatingActionButton fabEditNote;

        @BindView(R.id.noteCard)
        CardView noteCard;

        // CardView noteCard = (CardView) itemView.findViewById(R.id.noteCard);

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
