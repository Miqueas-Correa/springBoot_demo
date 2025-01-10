package ar.edu.utn.frbb.tup.springBoot_demo.model;

import java.util.List;

public class Movimientos {
    private Long numeroCuenta;
    private List<Transaccion> transacciones;

    public Movimientos() {}
    public Movimientos(Long numeroCuenta, List<Transaccion> transacciones) {
        this.numeroCuenta = numeroCuenta;
        this.transacciones = transacciones;
    }

    // Getters y Setters
    public Long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(numeroCuenta).append(";");
        for (Transaccion t : transacciones) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString().trim();
    }
}
