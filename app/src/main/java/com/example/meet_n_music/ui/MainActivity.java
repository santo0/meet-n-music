package com.example.meet_n_music.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.meet_n_music.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements DrawerController {
    BottomNavigationView bottomNav;
    NavController navController;
    NavHostFragment navHostFragment;
    Toolbar mToolbar;
    AppBarConfiguration appBarConfiguration;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //top bar
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);//Delete title

        //nav controller
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        navController = navHostFragment.getNavController();

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.nav_drawe_open, R.string.nav_drawe_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //...
 //       appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(findViewById(R.id.drawer_layout)).build();
 //       NavigationUI.setupActionBarWithNavController(this, navController, findViewById(R.id.drawer_layout));
        //Drawer navigation
//        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.navigation_view), navController);

        NavigationUI.setupWithNavController((NavigationView)findViewById(R.id.navigation_view), navController);


        //bottom bar
        bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);

        //top bar create event button
        ((ImageButton) mToolbar.findViewById(R.id.btn_create_event)).setOnClickListener(view -> {navController.navigate(R.id.action_feedFragment_to_creatingEventFragment);});
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void lockDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToolbar.setNavigationIcon(null);
    }

    @Override
    public void unlockDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
    }
}
