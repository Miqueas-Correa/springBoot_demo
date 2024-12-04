package ar.edu.utn.frbb.tup.springBoot_demo.model.dto;

public class CuentaDto {
    private String tipoCuenta;
    private String moneda;
    private long titular;
    private boolean estaA;
    private double saldo;

    // Getters and setters
    public boolean getEstaA() {
        return estaA;
    }
    public void setEstaA(boolean estaA) {
        this.estaA = estaA;
    }
    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    public String getTipoCuenta() {
        return tipoCuenta;
    }
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public long getTitular() {
        return titular;
    }
    public void setTitular(long dniTitular) {
        this.titular = dniTitular;
    }
}
