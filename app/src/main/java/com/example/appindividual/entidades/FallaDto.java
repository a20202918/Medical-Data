package com.example.appindividual.entidades;

public class FallaDto {

    //Clase DTO para los fallos de los equipos.
    private boolean fallo;
    private String comentario;

    public FallaDto(boolean fallo, String comentario) {
        this.fallo = fallo;
        this.comentario = comentario;
    }

    public FallaDto() {

    }

    public boolean isFallo() {
        return fallo;
    }

    public void setFallo(boolean fallo) {
        this.fallo = fallo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
