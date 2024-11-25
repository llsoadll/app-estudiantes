package com.example.demo;

//Estudiante
public class Estudiante {
    private String nombre;
    private String legajo;
    private String condicion;
    private String nota;
    private String cuatrimestre;
    private String anio;

    public Estudiante(String nombre, String legajo, String condicion, String nota,
                      String cuatrimestre, String anio) {
        this.nombre = nombre;
        this.legajo = legajo;
        this.condicion = condicion;
        this.nota = nota;
        this.cuatrimestre = cuatrimestre;
        this.anio = anio;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getLegajo() { return legajo; }
    public String getCondicion() { return condicion; }
    public String getNota() { return nota; }
    public String getCuatrimestre() { return cuatrimestre; }
    public String getAnio() { return anio; }
}
