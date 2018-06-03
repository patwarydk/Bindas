package com.example.mehrab_patwary.bindas;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsView extends Fragment {
    private DatabaseReference eventTbleRef;
    //   private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private Context context;

    public EventsView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.events_view, container, false);
        recyclerView = v.findViewById(R.id.eventRecyclerView);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        eventList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        eventTbleRef = mDatabaseRef.child("event_table");
        eventTbleRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Event e = (Event) d.getValue(Event.class);
                    eventList.add(e);
                }
                Toast.makeText(context, "Total events :"+eventList.size(), Toast.LENGTH_SHORT).show();
                adapter = new EventAdapter(eventList);
                LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
                Toast.makeText(context, "Failed :"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

       return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = getActivity();
        this.context = context;
    }

    }