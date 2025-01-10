package ar.edu.utn.frbb.tup.springBoot_demo.model;

import java.time.LocalDate;

public class Transaccion {
    private LocalDate fecha;
    private String tipo;
    private String descripcionBreve;
    private Double monto;

    public Transaccion() {}
    public Transaccion(String tipo, String descripcionBreve, Double monto) {
        this.fecha = LocalDate.now();
        this.tipo = tipo;
        this.descripcionBreve = descripcionBreve;
        this.monto = monto;
    }

    // Getters y Setters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return fecha + ";" + tipo + ";" + descripcionBreve + ";" + monto;
    }
}