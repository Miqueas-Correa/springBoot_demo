package ar.edu.utn.frbb.tup.springBoot_demo.model.dto;

public class CuentaDto {
    private String tipoCuenta;
    private String moneda;
    private Long titular;
    private Boolean estaA;
    private Double saldo;
    private String banco;

    // Getters and setters
    public String getBanco() {
        return this.banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public Boolean getEstaA() {
        return this.estaA;
    }
    public void setEstaA(Boolean estaA) {
        this.estaA = estaA;
    }
    public Double getSaldo() {
        return this.saldo;
    }
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    public String getTipoCuenta() {
        return this.tipoCuenta;
    }
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    public String getMoneda() {
        return this.moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public Long getTitular() {
        return this.titular;
    }
    public void setTitular(Long dniTitular) {
        this.titular = dniTitular;
    }
}
