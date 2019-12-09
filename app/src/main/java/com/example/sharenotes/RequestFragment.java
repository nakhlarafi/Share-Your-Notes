package com.example.sharenotes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class RequestFragment extends Fragment {

    View parentHolder;
    private ListView listView;
    DatabaseReference databaseReference;
    ArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentHolder = inflater.inflate(R.layout.fragment_request,container,false);
        listView = (ListView) parentHolder.findViewById(R.id.list_id);

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(parentHolder.getContext(),android.R.layout.simple_list_item_1,arrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String valueString = "";
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    //System.out.println(entry.getKey() + "/" + entry.getValue());
                    valueString = entry.getValue();
                    arrayList.add(valueString);
                }
                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return parentHolder;
    }

    Context cont;
    Activity a;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cont = context;


        if (context instanceof Activity){
            a=(Activity) context;
        }

    }
}
