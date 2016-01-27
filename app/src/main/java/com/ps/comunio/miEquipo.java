package com.ps.comunio;
import android.app.Application;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class miEquipo extends ListFragment {

    private ArrayList<Jugador> datos= new ArrayList<Jugador>();
    private ListView listado;
    private String userID;


    public miEquipo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        userID = getArguments().getString("userId");
        ObtDatos(this.getView());


        /*getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return true;
            }
        });*/
        return rootView;
    }

    public void CargarLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, datos);
        setListAdapter(adapter);
    }

    public void ObtDatos(View v){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://www.bbddremotaps.esy.es/php/activities/team.php";

        RequestParams parametros = new RequestParams();
        parametros.put("userId", userID);

        client.get(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) { //Todo ok
                    CargarLista(ObtDatosJSON(new String(responseBody)));
                } else
                    Toast.makeText(miEquipo.this.getContext(), "Status code != 200", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(miEquipo.this.getContext(), "ERROR FAIL ObtDatos", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public ArrayList<String> ObtDatosJSON(String response){
        ArrayList<String> listado = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            for (int i=0; i<jsonArray.length(); i++){              //Esto es el nombre que devuelve el php
                texto = jsonArray.getJSONObject(i).getString("name") + "    "+
                        jsonArray.getJSONObject(i).getString("rol") + " ";
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

    }


}