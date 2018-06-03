package com.example.mehrab_patwary.bindas;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event>eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.event_row,parent,false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
    holder.eventName.setText(eventList.get(position).getmEventName());
    holder.eventLocation.setText(eventList.get(position).getmEventLocation());
    holder.eventDescription.setText(eventList.get(position).getmEventDetails());
    holder.eventStartDay.setText(eventList.get(position).getmEventStartDate());
    holder.eventEndDay.setText(eventList.get(position).getmEventEndDate());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventLocation, eventDescription,
                eventStartDay, eventEndDay;
        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.viewEventsName);
            eventLocation = itemView.findViewById(R.id.viewEventsLocation);
            eventDescription = itemView.findViewById(R.id.viewEventsDetails);
            eventStartDay = itemView.findViewById(R.id.viewEventsStartDate);
            eventEndDay = itemView.findViewById(R.id.viewEventsEndDate);

        }
    }
}
