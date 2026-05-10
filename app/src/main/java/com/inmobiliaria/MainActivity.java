package com.inmobiliaria;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.inmobiliaria.request.ApiClient;
import com.inmobiliaria.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_logout) {

                mostrarLogout();

            }

            drawerLayout.closeDrawers();

            return true;
        });
    }

    private void mostrarLogout() {

        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Desea salir?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    ApiClient.guardarToken(this, "");

                    Intent intent =
                            new Intent(this, LoginActivity.class);

                    startActivity(intent);

                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}