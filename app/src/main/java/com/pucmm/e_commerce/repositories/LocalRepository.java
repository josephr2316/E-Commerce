package com.pucmm.e_commerce.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pucmm.e_commerce.database.CarritoCompras;

import static android.content.Context.MODE_PRIVATE;

public class LocalRepository {

    private static LocalRepository instance;

    public static LocalRepository getInstance() {
        if (instance == null){
            instance = new LocalRepository();
        }
        return instance;
    }
    public void guardarCarrito(SharedPreferences sharedPreferences, CarritoCompras carritoCompras) throws JsonProcessingException {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // Storing the key and its value as the data fetched from edittext
       myEdit.putString("carrito",  returnJsonFromObject(carritoCompras));
        myEdit.commit();
    }
    public CarritoCompras obtenerCarrito(SharedPreferences sharedPreferences){
        String object = sharedPreferences.getString("carrito", "");
        Log.i("Object", object);
        if(object.equals("")){
            return null;
        }
        return returnObjectFromJson(object);
    }
    public CarritoCompras returnObjectFromJson(String jsonInString) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(jsonInString, CarritoCompras.class);
    }
    public String returnJsonFromObject(CarritoCompras carritoCompras){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(carritoCompras);
    }
}
