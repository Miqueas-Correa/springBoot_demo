package ar.edu.utn.frbb.tup.springBoot_demo.model;
import java.time.LocalDateTime;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.dto.CuentaDto;

public class Cuenta {
    private Long numeroCuenta;
    private boolean estaA;
    private Long titular;
    private double saldo;
    private String tipoCuenta;
    private String moneda;
    private LocalDateTime fechaCreacion;

    // constructor
    public Cuenta(CuentaDto cuentaDto){
        this.estaA = true;
        this.titular = cuentaDto.getTitular();
        this.saldo = cuentaDto.getSaldo();
        this.tipoCuenta = cuentaDto.getTipoCuenta();
        this.moneda = cuentaDto.getMoneda();
        this.fechaCreacion = LocalDateTime.now();
    }

    // metodo toString
    @Override
    public String toString(){
        return this.numeroCuenta.toString() + ";" + this.titular.toString() + ";" + this.estaA + ";" + this.saldo + ";" + this.tipoCuenta + ";" + this.moneda.toString() + ";" + this.fechaCreacion.toString();
    }

    // Getters y Setters
    public boolean getEstaA() {
        return this.estaA;
    }
    public void setEstaA(boolean estaA) {
        this.estaA = estaA;
    }
    public Long getTitular() {
        return this.titular;
    }
    public void setTitular(Long id){
        this.titular = id;
    }
    public double getSaldo() {
        return this.saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    public Long getNumeroCuenta() {
        return this.numeroCuenta;
    }
    public void setNumeroCuenta(Long cbu_cvu) {
        this.numeroCuenta = cbu_cvu;
    }
    public String getTipoCuenta() {
        return this.tipoCuenta;
    }
    public void setTipoCuenta(String tipo_de_cuenta) {
        this.tipoCuenta = tipo_de_cuenta;
    }
    public String getMoneda() {
        return this.moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }
}