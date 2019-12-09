package com.example.sharenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.ApplicationController.TAG;

public class ProfileFragment extends Fragment implements AdapterView.OnItemClickListener {

    View parentHolder;
    String currentUserId,currentUserEmail,currentUserName;
    TextView nameText,emailText;
    ListView listView;
    ArrayAdapter arrayAdapter;
    Map<String, String[]> mapCheck = new HashMap<String, String[]>();

    DatabaseReference databaseReference;
    FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentHolder = inflater.inflate(R.layout.fragment_profile,container,false);

        nameText = (TextView) parentHolder.findViewById(R.id.my_name);
        emailText = (TextView) parentHolder.findViewById(R.id.my_email);
        listView = (ListView)parentHolder.findViewById(R.id.my_list_view);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        System.out.println(currentUserName);
        nameText.setText(currentUserName);
        emailText.setText(currentUserEmail);

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(parentHolder.getContext(),android.R.layout.simple_list_item_1,arrayList);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Members/"+currentUserId+"/Courses");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String valueString = dataSnapshot.getKey();
                arrayList.add(valueString);
                System.out.println(valueString);

                /*for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    valueString = postSnapshot.getKey();
                    System.out.println(valueString);
                    arrayList.add(valueString);
                }*/

                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                //List<String> list = new ArrayList<String>();
                String arr[] = new String[3];
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals("course_name")) arr[0] = entry.getValue();
                    else if (entry.getKey().equals("faculty")) arr[1] = entry.getValue();
                    else if (entry.getKey().equals("url")) arr[2] = entry.getValue();
                }
                mapCheck.put(valueString,arr);
                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);
                System.out.println("1 2 3"+dataSnapshot);

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
        listView.setOnItemClickListener(this);
        return parentHolder;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String courseId =(String) parent.getItemAtPosition(position);
        String newArray[] = mapCheck.get(courseId);

        String courseName = newArray[0];
        String faculty = newArray[1];
        String url = newArray[2];
        //System.out.println(lat+" "+longt);

        Intent intent = new Intent(cont, MyCourseDesc.class);
        intent.putExtra("id",courseId);
        intent.putExtra("name",courseName);
        intent.putExtra("faculty",faculty);
        intent.putExtra("url",url);
        startActivity(intent);
        Toast.makeText(cont,courseId, Toast.LENGTH_SHORT).show();

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
