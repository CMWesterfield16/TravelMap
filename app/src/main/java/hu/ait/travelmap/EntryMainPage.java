package hu.ait.travelmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hu.ait.travelmap.data.EntryData;


public class EntryMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_ENTRY_ID = "KEY_ENTRY_ID";
    private int entryEditIndex = -1;
    private EntryAdaptor entryAdapter;
    private RecyclerView recyclerViewEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_main_page);

        recyclerViewEntry = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewEntry.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewEntry.setLayoutManager(layoutManager);
        entryAdapter=  new EntryAdaptor(this);
        recyclerViewEntry.setAdapter(entryAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EntryMainPage.this, CreateEntryActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initPostsListener();
    }

    public void initPostsListener() {
        DatabaseReference entryRef = FirebaseDatabase.getInstance().getReference("entry");

        entryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                EntryData newEntry = dataSnapshot.getValue(EntryData.class);
                entryAdapter.addEntry(newEntry, dataSnapshot.getKey());
                recyclerViewEntry.smoothScrollToPosition(entryAdapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.entry_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.blogItem2) {
            Intent intentEntryView = new Intent();
            intentEntryView.setClass(this, EntryMainPage.class);
            this.startActivity(intentEntryView);

        } else if (id == R.id.newEntry2){
            Intent intentNewEntry = new Intent();
            intentNewEntry.setClass(this, CreateEntryActivity.class);
            this.startActivity(intentNewEntry);

        } else {
            Intent intentLargeMap = new Intent();
            intentLargeMap.setClass(this, FullMapsActivity.class);
            this.startActivity(intentLargeMap);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
