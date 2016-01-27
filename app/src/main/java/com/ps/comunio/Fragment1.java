package com.ps.comunio;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

import cz.msebera.android.httpclient.Header;

public class Fragment1 extends ListFragment {

    private String userID;


    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment1,container,false);
        ObtDatos(this.getView());
        return rootView;
    }

    public void CargarLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, datos);
        setListAdapter(adapter);
    }

    public void ObtDatos(View v){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://www.bbddremotaps.esy.es/php/activities/score.php";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) { //Todo ok
                    CargarLista(ObtDatosJSON(new String(responseBody)));
                } else
                    Toast.makeText(Fragment1.this.getContext(), "Status code != 200", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Fragment1.this.getContext(), "ERROR FAIL ObtDatos", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public ArrayList<String> ObtDatosJSON(String response){
        ArrayList<String> listado = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            for (int i=0; i<jsonArray.length(); i++){              //Esto es el nombre que devuelve el php
                texto = jsonArray.getJSONObject(i).getString("teamName") + "     "+
                        jsonArray.getJSONObject(i).getString("score") + " ";
                listado.add(texto);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listado;
    }

}