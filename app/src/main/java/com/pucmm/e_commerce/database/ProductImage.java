package com.pucmm.e_commerce.database;

import java.util.UUID;

public class ProductImage {
    private long id;
    private String imagen;

    public ProductImage() {
        
    }
    public ProductImage(long id, String imagen) {
        this.id = id;
        this.imagen = imagen;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
