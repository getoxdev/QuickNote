package com.quicknote.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quicknote.Database.NoteEntity;
import com.quicknote.EditorActivity;
import com.quicknote.MainActivity;
import com.quicknote.R;
import com.quicknote.ViewModels.ListViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupOnHoverListener;
import rm.com.longpresspopup.PopupStateListener;

import static com.quicknote.Utils.Constants.NOTE_ID_KEY;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context mContext;
    private List<NoteEntity> mNotesList;


    //containers for popupview
    private ConstraintLayout popupLayout;
    private Button delete_note;
    private String title, text;
    private TextView titlePopup, textPopup;


    public NotesAdapter(Context mContext, List<NoteEntity> mNotesList) {
        this.mContext = mContext;
        this.mNotesList = mNotesList;


    }





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.notes_item_layout,parent,false);
        mContext = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        final NoteEntity noteEntity = mNotesList.get(position);
        holder.noteText.setText(noteEntity.getText());
        holder.titleNote.setText(noteEntity.getTitle());

        //getting title and text for popup view
        title = noteEntity.getTitle();
        text = noteEntity.getText();

        //animation for appearance of notes in fading fashion
        Animation cardAnim = AnimationUtils.loadAnimation(this.mContext,R.anim.fade_card);
        holder.noteCard.setAnimation(cardAnim);

        holder.noteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditorActivity.class);
                intent.putExtra(NOTE_ID_KEY,noteEntity.getID());
                mContext.startActivity(intent);
            }
        });

        holder.onBind();




    }


    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    public NoteEntity getNoteAtPosition(int position)
    {
        return mNotesList.get(position);
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements  PopupInflaterListener,
            PopupStateListener, PopupOnHoverListener, View.OnClickListener
    {
        @BindView(R.id.note_text)
        TextView noteText;

        @BindView(R.id.title_note)
        TextView titleNote;

        @BindView(R.id.noteCard)
        CardView noteCard;

        LongPressPopup longPressPopup;
        private ListViewModel viewModel;





        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewModel = new ViewModelProvider((MainActivity)mContext).get(ListViewModel.class);

            ButterKnife.bind(this,itemView);
        }

        public void onBind(){
        if(longPressPopup != null && longPressPopup.isRegistered() ){
            longPressPopup.unregister();
        }

        longPressPopup = new LongPressPopupBuilder(mContext)
                    .setTarget(noteCard)
                    .setPopupView(R.layout.cardview_popup, this)
                    .setLongPressDuration(600)
                    .setDismissOnLongPressStop(true)
                    .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_CENTER)
                    .setPopupListener(this)
                    .setLongPressReleaseListener(this)
                    .setOnHoverListener(this)
                    .setCancelTouchOnDragOutsideView(true)
                    .setTag("note_popup")
                    .build();

        longPressPopup.register();


        }





        @Override
        public void onClick(View v) {
            delete_note = v.findViewById(R.id.delete_popup);
            if(delete_note != null){

                NoteEntity noteEntity = mNotesList.get(getAdapterPosition());
                viewModel.deleteNote(noteEntity);
                Toast.makeText(mContext, "Note Deleted", Toast.LENGTH_SHORT).show();


            }

        }

        @Override
        public void onViewInflated(@Nullable String popupTag, View root) {
            popupLayout = (ConstraintLayout) root.findViewById(R.id.popup_container);
            titlePopup = (TextView) root.findViewById(R.id.title_popup);
            textPopup = (TextView) root.findViewById(R.id.text_popup);





        }

        @Override
        public void onHoverChanged(View view, boolean isHovered) {

        }

        @Override
        public void onPopupShow(@Nullable String popupTag) {
            if(titlePopup != null){
                titlePopup.setText(mNotesList.get(getAdapterPosition()).getTitle());
            }
            if(textPopup != null){
                textPopup.setText(mNotesList.get(getAdapterPosition()).getText());
            }


        }

        @Override
        public void onPopupDismiss(@Nullable String popupTag) {

        }






    }



}
