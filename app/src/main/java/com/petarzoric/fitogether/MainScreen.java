package com.petarzoric.fitogether;

import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;


public class MainScreen extends AppCompatActivity  {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    ClipData.Item allUsers;
    private DatabaseReference usersDatabase;
    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        auth = FirebaseAuth.getInstance();
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(auth.getCurrentUser().getUid());

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
       // allUsers = (ClipData.Item) findViewById(R.id.allUsers);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Log.i("OnStart", "aufgerufen");
        usersDatabase.child("online").setValue(true);


    }

    @Override
    protected void onStop() {
        super.onStop();

        // Log.i("OnStop", "aufgerufen");
       // usersDatabase.child("online").setValue(false);
       // usersDatabase.child("online").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        usersDatabase.child("online").setValue(ServerValue.TIMESTAMP);

       // usersDatabase.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);

        // Log.i("OnPause", "aufgerufen");
        //usersDatabase.child("online").setValue(false);

    }
    @Override
    protected void onResume() {
        super.onResume();
      //  usersDatabase.child("online").setValue("true");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_studio_screen2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainScreen.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if(item.getItemId() == R.id.allUsers){

            Intent settingsIntent = new Intent(MainScreen.this, UsersActivity.class);
            startActivity(settingsIntent);
        }


        if(item.getItemId() == R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainScreen.this, MainActivity.class);
                    startActivity(intent);
                }



        return super.onOptionsItemSelected(item);

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    Tab1Dashboard tab1 = new Tab1Dashboard();
                    return tab1;
                case 1:
                    Tab2Calender tab2 = new Tab2Calender();
                    return tab2;

                case 2:
                    Tab3Search tab3 = new Tab3Search();
                    return tab3;
                case 3:
                    Tab4Chat tab4 = new Tab4Chat();
                    return tab4;

                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Dashboard";
                case 1:
                    return "Calender";

                case 2:
                    return "Search";

                case 3:
                    return "Chat";

            }
            return null;


        }
    }
}
