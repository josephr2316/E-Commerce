package com.pucmm.e_commerce.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper mapper = new ObjectMapper();
        myEdit.putString("carrito",  mapper.writeValueAsString(carritoCompras));
        myEdit.apply();
    }
    public CarritoCompras obtenerCarrito(SharedPreferences sharedPreferences){
        String object = sharedPreferences.getString("carrito", "");
        if(object.equals("")){
            return null;
        }
        try {
            return returnObjectFromJson(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public CarritoCompras returnObjectFromJson(String jsonInString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonInString, CarritoCompras.class);
    }
}
