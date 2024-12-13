package ar.edu.utn.frbb.tup.springBoot_demo.model;
import java.time.LocalDate;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;

public class Cuenta {
    private Long numeroCuenta;
    private Boolean estaA;
    private Long titular;
    private Double saldo;
    private String tipoCuenta;
    private String moneda;
    private LocalDate fechaCreacion;

    // constructor
    public Cuenta(){}
    public Cuenta(CuentaDto cuentaDto){
        this.estaA = true;
        this.titular = cuentaDto.getTitular();
        this.saldo = cuentaDto.getSaldo();
        this.tipoCuenta = cuentaDto.getTipoCuenta();
        this.moneda = cuentaDto.getMoneda();
        this.fechaCreacion = LocalDate.now();
    }

    // metodo toString
    @Override
    public String toString(){
        return this.numeroCuenta.toString() + ";" + this.titular.toString() + ";" + this.estaA + ";" + this.saldo + ";" + this.tipoCuenta + ";" + this.moneda.toString() + ";" + this.fechaCreacion.toString();
    }
    // metodo transferencia
    public void transferencia(Cuenta cuentaDestino, Double monto){
        this.saldo -= monto;
        cuentaDestino.saldo += monto;
    }
    public void depositar(Double monto){
        this.saldo += monto;
    }
    public void retirar(Double monto){
        this.saldo -= monto;
    }

    // Getters y Setters
    public Boolean getEstaA() {
        return this.estaA;
    }
    public void setEstaA(Boolean estaA) {
        this.estaA = estaA;
    }
    public Long getTitular() {
        return this.titular;
    }
    public void setTitular(Long id){
        this.titular = id;
    }
    public Double getSaldo() {
        return this.saldo;
    }
    public void setSaldo(Double saldo) {
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
    public LocalDate getFechaCreacion() {
        return this.fechaCreacion;
    }
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}