package com.example.debongz.nextldsaadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class OrderStages extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stages);
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        //fetchQuotes();


        if (bottomNavigation != null) {
            Intent in = getIntent();
            //String isFrag = in.getExtras().getString("frag");
            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigation.getMenu();
            menu.getItem(0);
            fragment = new OpenFragment();
            loadFragment(fragment);
        }
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_open:
                        fragment = new OpenFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_pull:
                        fragment = new PulledFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_deliver:
                        fragment = new DeiverdFragment();
                        loadFragment(fragment);
                        break;
                }
                return true;
            }
        });

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        //fetchQuotes();
        super.onResume();
    }
}
