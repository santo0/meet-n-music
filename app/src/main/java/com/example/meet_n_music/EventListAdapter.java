package com.example.meet_n_music;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.ui.FeedFragmentDirections;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private static final String TAG = "EventListAdapter";
    private ArrayList<Event> events;
    private final Context context;
    private EventItemAction action;

    public EventListAdapter(Context context, EventItemAction action) {
//        this.events = events;
        this.context = context;
        this.action = action;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.itemView.setTag(event);
        holder.name.setText(event.getName());
        holder.date.setText(event.getStartDate());
        holder.location.setText(event.getLocation());

        Log.d(TAG, "images/" + event.getImagePath());
        //Problemes
        FirebaseStorage.getInstance().getReference("images/" + event.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null) {
                    Glide.with(context)
                            .load(uri)
                            .centerCrop()
                            .into(holder.imageView);
                }else{
                    Glide.with(context)
                            .load(R.drawable._10804_live_music_comeback_mb_1314)
                            .centerCrop()
                            .into(holder.imageView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(context)
                        .load(R.drawable._10804_live_music_comeback_mb_1314)
                        .centerCrop()
                        .into(holder.imageView);

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Glide.with(context)
                        .load(R.drawable._10804_live_music_comeback_mb_1314)
                        .centerCrop()
                        .into(holder.imageView);

            }
        });


        holder.itemView.setOnClickListener(view -> {

//            FeedFragmentDirections.ActionFeedFragmentToViewEventFragment action = FeedFragmentDirections.actionFeedFragmentToViewEventFragment();
//            action.setEventId(event.getId());
            Navigation.findNavController(view).navigate(this.action.navigate(event.getId()));

        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, location;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvEventName);
            imageView = itemView.findViewById(R.id.ivEventImage);
            date = itemView.findViewById(R.id.tvEventDate);
            location = itemView.findViewById(R.id.tvEventLocation);
        }
    }
}
