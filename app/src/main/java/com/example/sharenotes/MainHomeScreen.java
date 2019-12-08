package com.example.sharenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainHomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DrawerLayout drawer;
    private List<ListItemsHomeHelper> listItemsHomeHelpers;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("Home");
        setContentView(R.layout.activity_main_home_screen);

        //New addition

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        imageView = (ImageView) hView.findViewById(R.id.profile_image);
        name = (TextView) hView.findViewById(R.id.user_name);
        email = (TextView) hView.findViewById(R.id.user_email);
        navigationView.setNavigationItemSelectedListener(this);
        members = new Members();

        Intent intent = getIntent();

        databaseReference = FirebaseDatabase.getInstance().getReference("Members")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference table =  databaseReference.child("Courses");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            //String personGivenName = acct.getGivenName();
            //String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personalName = acct.getDisplayName();
            System.out.println(personEmail+"***********");
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            //proImageView.setImageURI(personPhoto);
            Glide.with(this).load(String.valueOf(personPhoto))
                    .override(200,200)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
            email.setText(personEmail);
            name.setText(personalName);
        }
        else {
            personEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            personalName = intent.getStringExtra("name");
            email.setText(personEmail);
            name.setText(personalName);
        }

        /*signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.sign_out:
                        signOut();
                        break;
                    // ...
                }
            }
        });*/
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(uid);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Members").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    System.out.println(personEmail+"***********");
                    members.setEmail(personEmail);
                    members.setName(personalName);
                    databaseReference.setValue(members);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        uidRef.addListenerForSingleValueEvent(eventListener);





        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


      /*  listItemsHomeHelpers = new ArrayList<>();

        for (int i = 0;i<10;++i){
            ListItemsHomeHelper listItem = new ListItemsHomeHelper("Heading"+(i+1),
                    "Lorem Ipsum");
            listItemsHomeHelpers.add(listItem);
        }
        adapter = new AdapterForMainHome(listItemsHomeHelpers,this);
        recyclerView.setAdapter(adapter);*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                setTitle("Home");
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                setTitle("Profile");
                break;
            case R.id.nav_request:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RequestFragment()).commit();
                setTitle("Requests");
                break;
            case R.id.nav_tools:
                Toast.makeText(this,"Not available",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_signout:
                signOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //new Addition

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            logOut(button);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainHomeScreen.this,LoginActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Signed Out Succesfully",Toast.LENGTH_SHORT).show();
                        //finish();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainHomeScreen.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void goToMainHome(View view){
        Intent intent = new Intent(MainHomeScreen.this,MainHomeScreen.class);
        startActivity(intent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_with_home,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/


}
