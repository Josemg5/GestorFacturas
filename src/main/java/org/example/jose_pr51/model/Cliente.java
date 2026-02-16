package org.example.jose_pr51.model;
public class Cliente {
    private int id;
    private String nombre;
    private String apellidos;
    private String nif;

    public Cliente(int id, String nombre, String apellidos, String nif) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
}