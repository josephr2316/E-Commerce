package com.pucmm.e_commerce.database;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product implements Serializable {
    private String codigo;
    private String descripcion;
    private ArrayList<String> productImages;
    private String precio;
    public Product(){

    }

    public Product(String codigo, String descripcion, ArrayList<String> productImages, String precio) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.productImages = productImages;
        this.precio = precio;
    }
    public Product(String descripcion, ArrayList<String> productImages, String precio) {
        this.codigo = UUID.randomUUID().toString();
        this.descripcion = descripcion;
        this.productImages = productImages;
        this.precio = precio;
    }

    public Product(String descripcion, String precio) {
        this.codigo = UUID.randomUUID().toString();
        this.descripcion = descripcion;
        this.precio = precio;
    }
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<String> productImages) {
        this.productImages = productImages;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void generarCodigo(){
        this.codigo = UUID.randomUUID().toString();
    }

    public String getFirstImage(){
        return this.productImages.get(0);
    }
}
