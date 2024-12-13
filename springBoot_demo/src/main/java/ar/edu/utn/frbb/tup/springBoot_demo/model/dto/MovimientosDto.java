package ar.edu.utn.frbb.tup.springBoot_demo.model.dto;

public class MovimientosDto {
    private String descripcion;
    private Long numeroCuenta;

    // getters and setters
    public String getDescripcion() {
        return this.descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Long getNumeroCuenta() {
        return this.numeroCuenta;
    }
    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
