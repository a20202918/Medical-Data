package com.example.appindividual.entidades;

public class EquipoRV {

    //Clase para crear el Reycler View con el nombre de cada equipo.

    private String nombre;

    public EquipoRV(){

    }

    public EquipoRV(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}

