package com.ps.comunio;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Menuss extends AppCompatActivity {
    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    String username;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Intent logout = new Intent(this,MainActivity.class);
        /*UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1); //El 1 indica la versión, si siempre es 1, no volverá a crear la bbdd
        SQLiteDatabase db = usdbh.getWritableDatabase();
        if(db != null) {
            //Accedemos a la BD
            username = getIntent().getStringExtra("username");
            //Tab1.setText(username);
            setTitle(username);
        }*/

        username = getIntent().getStringExtra("username");
        userID = getIntent().getStringExtra("userId");


        appbar = (Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


        //Cambiar fragmento por uno de bienvenida
        Fragment fragment = new fragmentInicio();

        //final Fragment frJugadores = new Fragment1();
        final Fragment frEquipo = new FragmentoEquipo();

        /*Bundle param = new Bundle();
        param.putString("userId", userID);
        fragment.setArguments(param);*/

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        navView = (NavigationView)findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.Inicio:
                                fragment =  new fragmentInicio();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_listJugadores:
                                fragment = new Fragment1();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_miEquipo:
                                fragment = new miEquipo();
                                fragmentTransaction=true;
                                break;
                            case R.id.menu_mercado:
                                fragment = new mercado();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_fragmentjugar:
                                fragment = new fragmentjugar();
                                fragmentTransaction= true;
                                break;
                            case R.id.logout:
                                startActivity(logout);
                        }

                        if(fragmentTransaction) {
                            Bundle param = new Bundle();
                            param.putString("userId", userID);
                            fragment.setArguments(param);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            if(menuItem.getItemId() != R.id.Inicio) {
                                getSupportActionBar().setTitle(menuItem.getTitle());
                            }else {
                                //String usuario = ("Perfil de "+getGlobalUsuario());
                                username = getIntent().getStringExtra("username");
                                //Tab1.setText(username);
                                //setTitle(username);
                                getSupportActionBar().setTitle(username);
                            }
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getUserID(){
        return userID;
    }

    }



