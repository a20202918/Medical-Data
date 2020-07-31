package com.example.appindividual.entidades;

public class EquipoDto {

    //Clase DTO para el equipo biomédico
    private String nombre;
    private String tipo;
    private String marca;
    private String fechaMantenimiento;
    private String estado;

    public EquipoDto() {
    }

    public EquipoDto(String nombre, String tipo, String marca, String fechaMantenimiento, String estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.marca = marca;
        this.fechaMantenimiento = fechaMantenimiento;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(String fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
