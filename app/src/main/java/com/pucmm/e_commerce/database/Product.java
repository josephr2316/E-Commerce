package com.pucmm.e_commerce.database;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product {
    private String codigo;
    private String descripcion;
    private ArrayList<ProductImage> productImages;
    private Integer precio;
    public Product(){

    }

    public Product(String codigo, String descripcion, ArrayList<ProductImage> productImages, Integer precio) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.productImages = productImages;
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

    public ArrayList<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void generarCodigo(){
        this.codigo = UUID.randomUUID().toString();
    }
}
