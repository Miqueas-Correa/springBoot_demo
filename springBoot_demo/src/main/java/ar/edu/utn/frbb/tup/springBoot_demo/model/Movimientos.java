package ar.edu.utn.frbb.tup.springBoot_demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Movimientos {
    private Long id;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;
    private Long numeroCuenta;

    // getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    public Long getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    // metodos
    public void setMovimiento(String descripcion, Long numeroCuenta) {
        this.descripcion = descripcion;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.numeroCuenta = numeroCuenta;
    }
    // retornar movimiento
    @Override
    public String toString() {
        return this.id + ";" + this.numeroCuenta + ";" + this.fecha + ";" + this.hora + ";" + this.descripcion;
    }
}
