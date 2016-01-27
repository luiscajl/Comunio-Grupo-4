package com.ps.comunio;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class mercado extends ListFragment implements AdapterView.OnItemClickListener{

    private String userID;
    private ArrayList<String> listado;
    private String nombreJ;

    public mercado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mercado, container, false);
        listado = new ArrayList<>();
        userID = getArguments().getString("userId");
        ObtDatos(this.getView());

        return rootView;
    }

    public void CargarLista(ArrayList<String> datos){
        ArrayAdapter<String> adaptermerc = new ArrayAdapter<String>(getActivity(), R.layout.customlayout, R.id.listItemMerc, datos);
        setListAdapter(adaptermerc);
    }

    public void ObtDatos(View v){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://www.bbddremotaps.esy.es/php/activities/market.php";

        RequestParams parametros = new RequestParams();
        parametros.put("userId", userID);

        client.get(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) { //Todo ok
                    CargarLista(ObtDatosJSON(new String(responseBody)));
                } else
                    Toast.makeText(mercado.this.getContext(), "Status code != 200", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mercado.this.getContext(), "ERROR FAIL ObtDatos", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public ArrayList<String> ObtDatosJSON(String response){
        listado = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            for (int i=0; i<jsonArray.length(); i++){              //Esto es el nombre que devuelve el php
                texto = jsonArray.getJSONObject(i).getString("name") + "  Rol:    "+
                        jsonArray.getJSONObject(i).getString("rol") + " \nPrecio:  " + jsonArray.getJSONObject(i).getInt("price");
                listado.add(texto);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listado;
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        String getting = listado.get((int)id);
        nombreJ = getting.split(" Rol")[0];
        nombreJ = nombreJ.substring(0, nombreJ.length() -1);
        new AlertDialog.Builder(this.getContext())
                .setTitle("Fichar")
                .setMessage("¿Deseas fichar a este jugador?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Fichar jugador

                            AsyncHttpClient client = new AsyncHttpClient();
                            String url = "http://www.bbddremotaps.esy.es/php/activities/sign.php";

                            RequestParams parametros = new RequestParams();
                            parametros.put("userId", userID);
                            parametros.put("playerName", nombreJ);

                            client.get(url, parametros, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    if (statusCode == 200) { //Todo ok
                                        String codError;
                                        try {
                                            codError = ObtDatosFichaje(new String(responseBody));
                                            switch (codError){
                                                case "-1":
                                                    Toast.makeText(mercado.this.getContext(), "Try malfunction", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "1":
                                                    Toast.makeText(mercado.this.getContext(), "Error conexión", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "2":
                                                    Toast.makeText(mercado.this.getContext(), "No está en mercado fichajes", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "3":
                                                    Toast.makeText(mercado.this.getContext(), "No tienes dinero", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "4":
                                                    Toast.makeText(mercado.this.getContext(), "Error en la transacción", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "5":
                                                    Toast.makeText(mercado.this.getContext(), "Fichado!", Toast.LENGTH_SHORT).show();
                                                    break;
                                                default:
                                                    Toast.makeText(mercado.this.getContext(), "DEFAULT", Toast.LENGTH_SHORT).show();
                                            }
                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else
                                        Toast.makeText(mercado.this.getContext(), "Status code != 200", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast.makeText(mercado.this.getContext(), "ERROR FAIL ObtDatos", Toast.LENGTH_SHORT).show();
                                }
                            });


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public String ObtDatosFichaje(String response) throws JSONException {
        JSONObject fichaje = new JSONObject(response);
        String cod;
        cod = "-1";
        try{
            //Esto es el nombre que devuelve el php
                cod =  fichaje.getString("response");

        }catch(Exception e){
            e.printStackTrace();
        }
        return cod;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mercado.this.getContext(), "'OnItemClick'", Toast.LENGTH_SHORT).show();
    }
}