package com.example.sharenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //new additions
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //new
        parentHolder = inflater.inflate(R.layout.fragment_home,container,false);


        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(cont));

        listItemsHomeHelpers = new ArrayList<>();

        for (int i = 0;i<10;++i){
            ListItemsHomeHelper listItem = new ListItemsHomeHelper("Heading"+(i+1),
                    "Lorem Ipsum");
            listItemsHomeHelpers.add(listItem);
        }
        adapter = new AdapterForMainHome(listItemsHomeHelpers,cont);
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

}
