package com.example.meet_n_music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.ui.FeedFragmentDirections;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private ArrayList<Event> events;

    public EventListAdapter() {
//        this.events = events;
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
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvEventName);
        }
    }
}
