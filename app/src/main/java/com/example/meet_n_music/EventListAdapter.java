package com.example.meet_n_music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.ui.FeedFragmentDirections;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private ArrayList<Event> events;
    private Context context;

    public EventListAdapter(Context context) {
//        this.events = events;
        this.context = context;
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

        if (holder.imageView != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + event.getImagePath());
            Glide.with(context)
                    .load(storageReference)
                    .centerCrop()
                    .into((ImageView) holder.imageView);
        }
        holder.itemView.setOnClickListener(view -> {

            FeedFragmentDirections.ActionFeedFragmentToViewEventFragment action = FeedFragmentDirections.actionFeedFragmentToViewEventFragment();
            action.setEventId(event.getId());
            Navigation.findNavController(view).navigate(action);
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
