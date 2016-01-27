package com.ps.comunio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class testingbbdd extends AppCompatActivity {
    ListView listado;
    Button botonact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testingbbdd);
        listado = (ListView)findViewById(R.id.listbbdd);
        botonact = (Button)findViewById(R.id.btntestbbdd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_testingbbdd, menu);
        return true;
    }

    public void CargarLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        listado.setAdapter(adapter);
    }

    public void ObtDatos(View v){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://www.bbddremotaps.esy.es/php/activities/market.php";

        RequestParams parametros = new RequestParams();
        parametros.put("rol", "del");

        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) { //Todo ok
                    CargarLista(ObtDatosJSON(new String(responseBody)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }



    public ArrayList<String> ObtDatosJSON(String response){
        ArrayList<String> listado = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            for (int i=0; i<jsonArray.length(); i++){              //Esto es el nombre que devuelve el php
                texto = jsonArray.getJSONObject(i).getString("name") + " "+
                        jsonArray.getJSONObject(i).getString("rol") + " "+
                        jsonArray.getJSONObject(i).getString("price") + " ";
                listado.add(texto);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listado;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
