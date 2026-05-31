package com.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Pago implements Serializable {
    @SerializedName("idPago")
    private int idPago;
    @SerializedName("nroPago")
    private int nroPago;
    @SerializedName("idAlquiler")
    private int idAlquiler;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("importe")
    private double importe;

    public int getIdPago() { return idPago; }
    public int getNroPago() { return nroPago; }
    public int getIdAlquiler() { return idAlquiler; }
    public String getFecha() { return fecha; }
    public double getImporte() { return importe; }
}