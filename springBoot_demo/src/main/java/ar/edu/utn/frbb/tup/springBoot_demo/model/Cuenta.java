package ar.edu.utn.frbb.tup.springBoot_demo.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Cuenta {
    private boolean estaA;
    private Long id_del_titular;
    private double saldo;
    private String alias;
    private Long cbu_cvu;
    private String tipo_de_cuenta;
    private String moneda;
    private List<String> movimientos = new ArrayList<>();
    private LocalDateTime fecha_de_apertura;

    // buscar cuenta por el alias o por el cvu/cbu
    private Cuenta buscarCuenta_alias(String alias, Banco banco){
        for(Cliente cliente : banco.getClientes()){
            for (Cuenta cuenta2 : cliente.getCuentas()) {
                if (cuenta2.getEstaA() && cuenta2.getAlias().equals(alias)) {
                    return cuenta2;
                }
            }
        }
        return null; // null si no se encontro o no esta activa la cuenta
    }
    private Cuenta buscarCuenta_cvu_cbu(Long cvu, Banco banco){
        List<Cliente> listClientes = banco.getClientes();
        for(Cliente cliente : listClientes){
            Set<Cuenta> cuenta = cliente.getCuentas();
            for (Cuenta cuenta2 : cuenta) {
                if (cuenta2.estaA == true) {
                    if (cuenta2.cbu_cvu.equals(cvu)) {
                        return cuenta2;
                    }
                }
            }
        }
        return null; // null si no se encontro o no esta activa la cuenta
    }

    // verificar si el alia existe en una de las cuentas del banco
    public boolean buscarAlias(String alias){
        Banco banco = new Banco();
        List<Cliente> listClientes = banco.getClientes();
        for(Cliente cliente : listClientes){
            Set<Cuenta> cuenta = cliente.getCuentas();
            for (Cuenta cuenta2 : cuenta) {
                if (cuenta2.alias.equals(alias)) {
                    return true;
                }
            }
        }
        return false;
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
    public Long getId_del_titular() {
        return this.id_del_titular;
    }
    public void setId_del_titular(Long id){
        this.setMovimiento("Se cambio el id de la cuenta de: " + this.id_del_titular + " a: " + id);
        this.id_del_titular = id;
    }
    public double getSaldo() {
        return this.saldo;
    }
    public void setSaldo(double saldo) {
        this.setMovimiento("Se cambio el saldo de la cuenta de: " + this.saldo + " a: " + saldo);
        this.saldo = saldo;
    }
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.setMovimiento("Se cambio el alias de la cuenta de: " + this.alias + " a: " + alias);
        this.alias = alias;
    }
    public Long getCbu_cvu() {
        return this.cbu_cvu;
    }
    public void setCbu_cvu(Long cbu_cvu) {
        this.setMovimiento("Se cambio el cbu_cvu de la cuenta de: " + this.cbu_cvu + " a: " + cbu_cvu);
        this.cbu_cvu = cbu_cvu;
    }
    public String getTipo_de_cuenta() {
        return this.tipo_de_cuenta;
    }
    public void setTipo_de_cuenta(String tipo_de_cuenta) {
        this.setMovimiento("Se cambio el tipo de cuenta de la cuenta de: " + this.tipo_de_cuenta + " a: " + tipo_de_cuenta);
        this.tipo_de_cuenta = tipo_de_cuenta;
    }
    public String getMoneda() {
        return this.moneda;
    }
    public void setMoneda(String moneda) {
        this.setMovimiento("Se cambio la moneda de la cuenta de: " + this.moneda + " a: " + moneda);
        this.moneda = moneda;
    }
    public String getFecha_de_apertura() {
        return this.fecha_de_apertura.toString();
    }
    public void setFecha_de_apertura(LocalDateTime fecha_de_apertura) {
        this.setMovimiento("Se asigno la fecha de apertura de la cuenta a: " + fecha_de_apertura.toString());
        this.fecha_de_apertura = fecha_de_apertura;
    }


    // Métodos
    public void getDatosCuenta(){
        System.out.println("------------------------");
        System.out.println("DATOS DEL CUENTA");
        System.out.println("Id del titular: "+ this.id_del_titular);
        System.out.println("Esta activa: " + this.estaA);
        System.out.println("Saldo: " + this.saldo);
        System.out.println("Alias: " + this.alias);
        System.out.println("CBU/CVU: " + this.cbu_cvu);
        System.out.println("Tipo de cuenta: " + this.tipo_de_cuenta);
        System.out.println("Moneda: " + this.moneda);
        System.out.println("Fecha de apertura: " + this.fecha_de_apertura);
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
    public void transferir(double efectivo, String alias, Long cvu, Banco banco) {
        Cuenta cuentaA = buscarCuenta_alias(alias, banco);
        Cuenta cuentaC = buscarCuenta_cvu_cbu(cvu, banco);
        // Verifica que la cuenta actual esté activa y que no se transfiera a sí misma
        if (this.estaA && (!this.alias.equals(alias) || !this.cbu_cvu.equals(cvu))) {
            if (cuentaA != null || cuentaC != null) {
                Cuenta cuentaDestino = (cuentaA != null) ? cuentaA : cuentaC;
                if(this.saldo >= efectivo){
                    this.descontarDinero(efectivo);
                    cuentaDestino.recibirTransferencia(efectivo, this.getAlias(), this.getMoneda());
                    this.setMovimiento("Transferencia de " + efectivo + " " + cuentaDestino.moneda + " a " + cuentaDestino.getAlias());
                } else {
                    System.out.println("No hay saldo suficiente");
                }
            } else {
                System.out.println("No se encontró la cuenta o no está activa");
            }
        } else {
            System.out.println("Tu cuenta no está activa o estás intentando transferir a tu propia cuenta.");
        }
    }
    // metodo para llebar registro de las transferecias recibidas
    public void recibirTransferencia(double efectivo, String alias, String moneda) {
        this.deposito(efectivo);
        this.setMovimiento("Transferencia de " + efectivo + " " + moneda + " de " + alias); // Registra el movimiento
    }
}