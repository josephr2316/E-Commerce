package com.pucmm.e_commerce.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CarritoCompras implements Serializable {
    private String id;
    private HashMap<String, Integer> productArrayList;
    private String userID;

    public CarritoCompras(){
        this.productArrayList = new HashMap<>();
        this.id = UUID.randomUUID().toString();
    }
    public CarritoCompras(String userID) {
        this.productArrayList = new HashMap<>();
        this.id = UUID.randomUUID().toString();
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setProductID(String product, Integer cantidad){
        //TODO: Hay que ponerle que se sube con la cantidad que tiene existente
        this.productArrayList.put(product, cantidad);
    }
    public Integer getCantidadProducto(String product) {
        return productArrayList.get(product);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
