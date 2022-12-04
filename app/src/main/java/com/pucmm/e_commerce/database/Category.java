package com.pucmm.e_commerce.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Category implements Serializable {
    private String id;
    private String nombre;
    private String imagen;

    private ArrayList<Product> productList;
    public Category(){
        this.id = UUID.randomUUID().toString();
        this.productList = new ArrayList<>();
    }
    public Category(String nombre, String imagen) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.imagen = imagen;
    }
    public Category(String nombre){
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.imagen = UUID.randomUUID().toString();
    }
    public String getId(){
        return this.id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
    public void addProduct(Product product) {
        this.productList.add(product);
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public void generarImagen(){
        this.imagen = UUID.randomUUID().toString();
    }
}
