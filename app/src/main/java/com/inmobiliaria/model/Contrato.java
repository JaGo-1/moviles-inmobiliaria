package com.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contrato implements Serializable {
    @SerializedName("idContrato")
    private int idContrato;
    @SerializedName("fechaInicio")
    private String fechaInicio;
    @SerializedName("fechaFin")
    private String fechaFin;
    @SerializedName("monto")
    private double monto;
    @SerializedName("inquilino")
    private Inquilino inquilino;

    public Contrato() {
    }

    public Contrato(String fechaFin, String fechaInicio, int idContrato, Inquilino inquilino, double monto) {
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.idContrato = idContrato;
        this.inquilino = inquilino;
        this.monto = monto;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
