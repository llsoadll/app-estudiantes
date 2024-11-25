package com.example.demo;

// Nueva clase para manejar usuarios
public class Usuario {
    private String nombre;
    private String contrasena;

    // Constructor por defecto
    public Usuario() {
        this.nombre = "";
        this.contrasena = "";
    }

    // Constructor con parámetros
    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    // Getter para nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para contraseña
    public String getContrasena() {
        return contrasena;
    }

    // Setter para contraseña
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}