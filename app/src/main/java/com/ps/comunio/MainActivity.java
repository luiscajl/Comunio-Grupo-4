package com.ps.comunio;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    private String user; //Para que la otra actividad pueda acceder al nombre, tiene que ser pública y estática, pero esto puede ocasionar problemas en el momento en que se pueda cerrar sesión y logearse con otro user
    private String contra;
    TextView login;
    TextView pswd;
    Button boton;
    CheckBox chkReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Parte 2
        setContentView(R.layout.activity_login);
        chkReg = (CheckBox)findViewById(R.id.chkBoxRegistrar);
        login = (TextView)findViewById(R.id.txtLogin);
        pswd = (TextView)findViewById(R.id.txtPswd);
        boton = (Button)findViewById(R.id.btnLogin);
        //UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);



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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }


    public void testlogin(View v) throws InterruptedException {
        //Abrimos / Creamos la bd
        //UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1); //El 1 indica la versión, si siempre es 1, no volverá a crear la bbdd
        //SQLiteDatabase db = usdbh.getWritableDatabase();
        user = login.getText().toString();
        contra = pswd.getText().toString();
        String equipo = "Eq"+user;
        if (chkReg.isChecked()){
            Registrarse(user, contra,equipo );
            //Hacer registro
        }else { //LOGIN
            Logearse(user, contra);
        }
    }


    public String checklogin(String response){
        String id = null;
        try{
            JSONObject jsono = new JSONObject(response);
            //Esto es el id que devuelve el php
                id = jsono.getString("userId");
        }catch(Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public void Logearse(String username, String password){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://www.bbddremotaps.esy.es/php/activities/logIn.php";

        RequestParams params = new RequestParams();
        params.put("name", username);
        //Toast.makeText(MainActivity.this, "'"+username+"'",Toast.LENGTH_LONG).show();
        params.put("psw", password);
        //Toast.makeText(MainActivity.this, "'"+params.toString()+"'",Toast.LENGTH_LONG).show();
        //Toast.makeText(MainActivity.this, "'"+url+params+"'",Toast.LENGTH_LONG).show();

        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String id = checklogin(new String(responseBody));
                //Toast.makeText(MainActivity.this, "'"+id+"'",Toast.LENGTH_SHORT).show();
                if ((id != null) && (id != "false")) {
                    Toast.makeText(MainActivity.this, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Menuss.class);
                    intent.putExtra("username", user);
                    intent.putExtra("userId", id);//mandamos el userId para poder usarlo
                    startActivity(intent);
                } else
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "ERROR FAIL Logearse", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void Registrarse(String username, String password, String equipo){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://www.bbddremotaps.esy.es/php/activities/checkIn.php?";
        String mail = username+"mail@mail.com";

        String params = "name="+username+"&psw="+password+"&teamName="+equipo+"&email="+mail;

        client.post(url+params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    //Se ha registrado correctamente
                    Toast.makeText(MainActivity.this, "Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Menuss.class);
                    intent.putExtra("username",user);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this, "ERROR en Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "ERROR FAIL Registrarse",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void RegisterOnOff(View v){
        if (chkReg.isChecked()) {
            boton.setText("Registrar");
            //Escribir en base de datos o fichero
        }else
            boton.setText("Aceptar");
    }


}

