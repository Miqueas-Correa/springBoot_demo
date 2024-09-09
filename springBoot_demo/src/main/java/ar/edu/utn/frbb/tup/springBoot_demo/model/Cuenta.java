package ar.edu.utn.frbb.tup.springBoot_demo.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.InactiveAccountException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.InsufficientFundsException;

public class Cuenta {
    private boolean estaA;
    private Long titular;
    private double saldo;
    private Long numeroCuenta;
    private String tipoCuenta;
    private String moneda;
    private List<String> movimientos = new ArrayList<>();
    private LocalDateTime fechaCreacion;

    // metodo toString
    @Override
    public String toString(){
        return this.titular.toString() + ";" + this.estaA + ";" + this.saldo + ";" + this.numeroCuenta.toString() + ";" + this.tipoCuenta + ";" + this.moneda.toString() + ";" + this.movimientos.toString() + ";" + this.fechaCreacion.toString();
    }

    // Getters y Setters
    public List<String> getMovimientos() {
        return this.movimientos;
    }
    public void setMovimientos(List<String> movimientos) {
        this.movimientos = movimientos;
    }
    private void setMovimiento(String movimiento) {
        this.movimientos.add(movimiento);
    }
    public boolean getEstaA() {
        return this.estaA;
    }
    public void setEstaA(boolean estaA) {
        this.setMovimiento("Estado de la cuenta paso de: " + this.estaA + " a: " + estaA);
        this.estaA = estaA;
    }
    public Long getTitular() {
        return this.titular;
    }
    public void setTitular(Long id){
        this.setMovimiento("Se cambio el id de la cuenta de: " + this.titular + " a: " + id);
        this.titular = id;
    }
    public double getSaldo() {
        return this.saldo;
    }
    public void setSaldo(double saldo) {
        this.setMovimiento("Se cambio el saldo de la cuenta de: " + this.saldo + " a: " + saldo);
        this.saldo = saldo;
    }
    public Long getNumeroCuenta() {
        return this.numeroCuenta;
    }
    public void setNumeroCuenta(Long cbu_cvu) {
        this.setMovimiento("Se establecio el numero de la cuenta a: " + cbu_cvu);
        this.numeroCuenta = cbu_cvu;
    }
    public String getTipoCuenta() {
        return this.tipoCuenta;
    }
    public void setTipoCuenta(String tipo_de_cuenta) {
        this.setMovimiento("Se cambio el tipo de cuenta de la cuenta de: " + this.tipoCuenta + " a: " + tipo_de_cuenta);
        this.tipoCuenta = tipo_de_cuenta;
    }
    public String getMoneda() {
        return this.moneda;
    }
    public void setMoneda(String moneda) {
        this.setMovimiento("Se cambio la moneda de la cuenta de: " + this.moneda + " a: " + moneda);
        this.moneda = moneda;
    }
    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fecha_de_apertura) {
        this.setMovimiento("Se asigno la fecha de apertura de la cuenta a: " + fecha_de_apertura.toString());
        this.fechaCreacion = fecha_de_apertura;
    }


    // Métodos
    public void getDatosCuenta(){
        System.out.println("------------------------");
        System.out.println("DATOS DEL CUENTA");
        System.out.println("Id del titular: "+ this.titular);
        System.out.println("Esta activa: " + this.estaA);
        System.out.println("Saldo: " + this.saldo);
        System.out.println("CBU/CVU: " + this.numeroCuenta);
        System.out.println("Tipo de cuenta: " + this.tipoCuenta);
        System.out.println("Moneda: " + this.moneda);
        System.out.println("Fecha de apertura: " + this.fechaCreacion);
        System.out.println("---------------------------");
    }
    public void deposito(double efectivo){
        if (this.estaA) {
            this.saldo += efectivo;
            this.setMovimiento("Deposito de " + efectivo + " " + this.moneda);
        }else{
            System.out.println("Tu cuenta no esta activa");
        }
    }
    private void descontarDinero(double efectivo){
        this.saldo -= efectivo;
        this.setMovimiento("Descuento de " + efectivo + " " + this.moneda);
    }
    // metodo para hacer un retiro
    public void retiro(double efectivo){
        if (this.estaA) {
            if(this.saldo >= efectivo){
                this.descontarDinero(efectivo);
                this.setMovimiento("Retiro de " + efectivo + " " + this.moneda);
            }
            else{
                System.out.println("No hay saldo suficiente");
            }
        }else{
            System.out.println("Tu cuenta no esta activa");
        }
    }
    // metodo para hacer una transferencia
    public void transferir(double monto, Long cuentaDestino) throws InactiveAccountException, InsufficientFundsException {
        // Verifica que la cuenta actual esté activa y que no se transfiera a sí misma
        if (!estaA) {throw new InactiveAccountException("La cuenta no está activa");}
        if (cuentaDestino == this.titular) {throw new InactiveAccountException("La cuenta de destino es la misma cuenta");}
        if (cuentaDestino == null) {throw new InactiveAccountException("La cuenta de destino no existe");}
        // verifico el saldo de la cuenta
        if (this.saldo < monto) {throw new InsufficientFundsException("No hay saldo suficiente");}
    }
    // metodo para llebar registro de las transferecias recibidas
    public void recibirTransferencia(double efectivo, String alias, String moneda) {
        this.deposito(efectivo);
        this.setMovimiento("Transferencia de " + efectivo + " " + moneda + " de " + alias); // Registra el movimiento
    }
}