package com.pucmm.e_commerce.database;

import java.util.List;
import java.util.UUID;

public class Category {
    private String nombre;
    private String imagen;

    private List<Product> productList;
    public Category(){

    }
    public Category(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
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

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public void generarImagen(){
        this.imagen = UUID.randomUUID().toString();
    }
}
