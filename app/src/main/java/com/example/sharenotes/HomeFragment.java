package com.example.sharenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    //new additions
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    private DrawerLayout drawer;
    private List<ListItemsHomeHelper> listItemsHomeHelpers;
    View parentHolder;

    //new
    private FirebaseAuth.AuthStateListener authStateListener;
    TextView name,email;
    Button button,signOut;
    ImageView imageView;
    GoogleSignInClient googleSignInClient;
    Members members;
    DatabaseReference databaseReference;
    String personEmail,personalName;
    private FirebaseAuth mAuth;
    public static String userId;
    AdapterForMainHome adapter;
    ArrayList<String> arrList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //new
        parentHolder = inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(cont));
        arrList = new ArrayList<>();
        //adapter = new AdapterForMainHome(cont,recyclerView,new ArrayList<String>(),new ArrayList<String>());
        setHasOptionsMenu(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //called for individual items at the database reference
                /*String filename = dataSnapshot.getKey();
                String url = dataSnapshot.getValue(String.class); //url for file name
                ((AdapterForMainHome)recyclerView.getAdapter()).update(filename,url);*/

                /*for (DataSnapshot memberSnapshot : dataSnapshot.getChildren()){

                }*/

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                //Log.d(TAG, "Value is: " + map);
                //Map<String, String> map = ...
                String name = "",email="";
                //Object courseObj =  new Object();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    //System.out.println(entry.getKey() + "/" + entry.getValue());
                    Object obj = entry.getValue();
                    Gson gson = new Gson();
                    String json = gson.toJson(obj);
                    Object courseObj =  new Object();
                    if (obj!=null){
                        try {
                            JSONObject myObjToConvert = new JSONObject(json);
                            name = myObjToConvert.getString("name");
                            email = myObjToConvert.getString("email");
                            courseObj = myObjToConvert.getString("Courses");
                            System.out.println(courseObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //Gson gson2 = new Gson();
                    String courseName = "";

                    //String json2 = gson2.toJson(courseObj);
                    Object descObject = new Object();
                    String arr[] = new String[3];
                    if (courseObj!=null){
                        try {
                            HashMap<String, Object> yourHashMap = new Gson().fromJson(courseObj.toString(), HashMap.class);
                            for (Map.Entry<String, Object> entry2 : yourHashMap.entrySet()) {
                                //System.out.println(entry.getKey() + "/" + entry.getValue());
                                courseName = entry2.getKey();
                                arrList.add(courseName);
                                descObject = entry2.getValue();
                                Object nObj = gson.toJson(descObject);

                                HashMap<String, String> yourHashMap2 = new Gson().fromJson(nObj.toString(), HashMap.class);
                                int i = 0;
                                for (Map.Entry<String, String> entry3 : yourHashMap2.entrySet()) {
                                    //System.out.println(entry.getKey() + "/" + entry.getValue());
                                    arr[i] = entry3.getValue();
                                    System.out.println(arr[i]+"*******"+i);
                                    ++i;
                                }

                                ((AdapterForMainHome)recyclerView.getAdapter()).update(courseName ,arr[2],arr[1],arr[0],name);
                            }



                        }catch (Exception e){

                        }
                    }

                    //((AdapterForMainHome)recyclerView.getAdapter()).update(courseName ," ","nakhla");
                }
                //((AdapterForMainHome)recyclerView.getAdapter()).update("CSE" ,"kisu ekta","nakhla");
                //adapter = new AdapterForMainHome(cont,recyclerView,arrList,new ArrayList<String>());

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
        System.out.println(Arrays.toString(new ArrayList[]{arrList}));
        adapter = new AdapterForMainHome(cont,recyclerView,new ArrayList<String>(),new ArrayList<String>());

        listItemsHomeHelpers = new ArrayList<>();

        /*for (int i = 0;i<10;++i){
            ListItemsHomeHelper listItem = new ListItemsHomeHelper("Heading"+(i+1),
                    "Lorem Ipsum");
            listItemsHomeHelpers.add(listItem);
        }*/
        //adapter = new AdapterForMainHome(cont,recyclerView,new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(adapter);

        FloatingActionButton button = (FloatingActionButton) parentHolder.findViewById(R.id.add_btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(a,ChooseOptions.class);
                startActivity(intent);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


        inflater.inflate(R.menu.navigation_with_home, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println("kisu ekta ************");
                adapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.navigation_with_home,menu);
    }
}
