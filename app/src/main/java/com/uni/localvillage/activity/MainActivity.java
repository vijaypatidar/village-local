package com.uni.localvillage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.uni.localvillage.R;
import com.uni.localvillage.Utils;
import com.uni.localvillage.fragments.BookingFragment;
import com.uni.localvillage.fragments.CategoriesFragment;
import com.uni.localvillage.fragments.RequestFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private CategoriesFragment categoriesFragment;
    private FirebaseAuth firebaseAuth;
    private NavigationView navigationView;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share with friends", Snackbar.LENGTH_LONG)
                        .setAction("Share", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Village Local");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, "link to download and detail");
                                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                            }
                        }).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();
        drawer.addDrawerListener(toggle);
        fragmentManager = getSupportFragmentManager();
        initFragments();
        showHome();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uid = firebaseAuth.getUid();
        prepareStateAuthUI();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            showHome();
        } else if (id == R.id.nav_logIn) {
            startActivity(new Intent(this, AuthActivity.class));
        } else if (id == R.id.nav_logOut) {
            firebaseAuth.signOut();
            uid = null;
            prepareStateAuthUI();
        } else if (id == R.id.edit_profile) {
            if (uid != null && !uid.isEmpty()) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                showHome();
                Toast.makeText(this, "login to edit profile", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_booking) {
            if (uid != null && !uid.isEmpty()) {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new BookingFragment()).commit();
            } else {
                showHome();
                Toast.makeText(this, "login to edit profile", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_request) {
            if (uid != null && !uid.isEmpty()) {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new RequestFragment()).commit();
            } else {
                showHome();
                Toast.makeText(this, "login to edit profile", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void initFragments() {
        categoriesFragment = new CategoriesFragment();
    }

    private void prepareLoggedUserSpecificUI() {
        if (!Utils.getValue(this, Utils.TYPE).equals("provider")) {
            navigationView.getMenu().findItem(R.id.nav_request).setVisible(false);
        }
    }

    private void prepareStateAuthUI() {
        if (firebaseAuth.getCurrentUser() == null) {
            navigationView.getMenu().findItem(R.id.nav_logOut).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logIn).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_logOut).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logIn).setVisible(false);

            View view = navigationView.getHeaderView(0);
            TextView name = view.findViewById(R.id.nav_user_name);
            TextView email = view.findViewById(R.id.nav_user_email);
            name.setText(Utils.getValue(this, Utils.NAME));
            email.setText(Utils.getValue(this, Utils.EMAIL));

            prepareLoggedUserSpecificUI();
        }

    }

    private void showHome() {
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, categoriesFragment).commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

}
