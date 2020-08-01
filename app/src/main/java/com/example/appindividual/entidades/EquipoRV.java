package com.example.appindividual.entidades;

public class EquipoRV {

    //Clase para crear el Reycler View con el nombre de cada equipo.

    private int id;
    private String nombre;

    public EquipoRV(){

    }

    public EquipoRV(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

