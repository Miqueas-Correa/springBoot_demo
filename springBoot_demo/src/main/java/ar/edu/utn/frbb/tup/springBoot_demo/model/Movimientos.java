package ar.edu.utn.frbb.tup.springBoot_demo.model;

import java.time.LocalDate;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;

public class Movimientos {
    private Long id;
    private String descripcion;
    private LocalDate fecha_y_hs;
    private Long numeroCuenta;

    // constructor
    public Movimientos(){}
    public Movimientos(MovimientosDto movimientosDto){
        this.descripcion = movimientosDto.getDescripcion();
        this.numeroCuenta = movimientosDto.getNumeroCuenta();
        this.fecha_y_hs = LocalDate.now();
    }

    // getters and setters
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDate getFecha_y_hs() {
        return this.fecha_y_hs;
    }
    public void setFecha_y_hs(LocalDate fecha) {
        this.fecha_y_hs = fecha;
    }
    public Long getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    // retornar movimiento
    @Override
    public String toString() {
        return this.id + ";" + this.numeroCuenta + ";" + this.fecha_y_hs + ";" + this.descripcion;
    }
}
