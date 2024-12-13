package ar.edu.utn.frbb.tup.springBoot_demo.model.dto;

public class TransferirDto {
    private Long cuentaOrigen;
    private Long cuentaDestino;
    private double monto;
    private String moneda;

    public Long getCuentaOrigen() {
        return this.cuentaOrigen;
    }
    public void setCuentaOrigen(Long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }
    public Long getCuentaDestino() {
        return this.cuentaDestino;
    }
    public void setCuentaDestino(Long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }
    public double getMonto() {
        return this.monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public String getMoneda() {
        return this.moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}