package com.inmobiliaria.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Propietario implements Serializable {
    private int idPropietario;
    private String nombre, apellido, dni, telefono, email;

    public Propietario() {}

    public Propietario(String apellido, String dni, String email, int idPropietario, String nombre, String telefono) {
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.idPropietario = idPropietario;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @NonNull
    @Override
    public String toString() {
        return "Propietario{" +
                "apellido='" + apellido + '\'' +
                ", idPropietario=" + idPropietario +
                ", nombre='" + nombre + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
