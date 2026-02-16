package org.example.jose_pr51.model;

public class Factura {
    private int idFactura;
    private String fecha;

    public Factura(int idFactura, String fecha) {
        this.idFactura = idFactura;
        this.fecha = fecha;
    }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}