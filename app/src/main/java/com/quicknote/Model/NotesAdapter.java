package com.quicknote.Model;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.platform.MaterialArcMotion;
import com.google.android.material.transition.platform.MaterialContainerTransform;
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

public class NotesAdapter extends ListAdapter<NoteEntity,NotesAdapter.MyViewHolder> {

    private Context mContext;
//    private List<NoteEntity> mNotesList;


    //containers for popupview
    private ConstraintLayout popupLayout;
    private Button delete_note;
    private String title, text;
    private TextView titlePopup, textPopup;
    private LottieAnimationView animationView;

        //TODO: To be deleted as its of no use
//    public NotesAdapter(Context mContext, List<NoteEntity> mNotesList) {
//        this();
//        this.mContext = mContext;
////        this.mNotesList = mNotesList;
//
//
//    }

    public NotesAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<NoteEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem.getID() == newItem.getID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getText().equals(newItem.getText())
                    && oldItem.getDate().toString().equals(newItem.getDate().toString());
        }
    };


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item_layout,parent,false);
        mContext = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        final NoteEntity noteEntity = getItem(position);
        holder.noteText.setText(noteEntity.getText());
        holder.titleNote.setText(noteEntity.getTitle());

        //Material container transform on notecard
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setDuration(400);
        transform.addTarget(holder.noteCard);
        transform.setPathMotion(new MaterialArcMotion());

        //getting title and text for popup view
        title = noteEntity.getTitle();
        text = noteEntity.getText();


        holder.noteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditorActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, holder.noteCard, "fab_expand");
                intent.putExtra(NOTE_ID_KEY,noteEntity.getID());
                mContext.startActivity(intent, options.toBundle());
            }
        });

        holder.onBind();




    }

    //TODO: to be deleted as these methods are no longer used
//    @Override
//    public int getItemCount() {
//        return mNotesList.size();
//    }

//    public void setNotes(List<NoteEntity> notes){
//        mNotesList = notes;
//        notifyDataSetChanged();
//    }

    public NoteEntity getNoteAtPosition(int position)
    {
        return getItem(position);
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
                //delete_note.setBackgroundColor(Color.parseColor("#ADCED9"));
                NoteEntity noteEntity = getItem(getAdapterPosition());
                viewModel.deleteNote(noteEntity);
                Toast.makeText(mContext, "Note Deleted", Toast.LENGTH_SHORT).show();


            }

        }

        @Override
        public void onViewInflated(@Nullable String popupTag, View root) {
            popupLayout = (ConstraintLayout) root.findViewById(R.id.popup_container);
            titlePopup = (TextView) root.findViewById(R.id.title_popup);
            textPopup = (TextView) root.findViewById(R.id.text_popup);
            delete_note = (Button) root.findViewById(R.id.delete_popup);







        }

        @Override
        public void onHoverChanged(View view, boolean isHovered) {

          if(isHovered){
              if(view.getId() == delete_note.getId()){
                  delete_note.setBackgroundColor(Color.parseColor("#97B7F0"));

              }
              else{
                  delete_note.setBackgroundColor(Color.parseColor("#CADFE6"));
              }
          }

        }

        @Override
        public void onPopupShow(@Nullable String popupTag) {
            if(titlePopup != null){
                titlePopup.setText(getItem(getAdapterPosition()).getTitle());
            }
            if(textPopup != null){
                textPopup.setText(getItem(getAdapterPosition()).getText());
            }


        }

        @Override
        public void onPopupDismiss(@Nullable String popupTag) {

        }






    }



}
