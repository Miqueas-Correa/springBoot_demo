package ar.edu.utn.frbb.tup.springBoot_demo.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.AccountNotFoundException;

public class Cliente extends Persona {
    // atributos para id, cbu/cvu y alias unicos de las cuentas
    private Set<String> idsYaUsados = new HashSet<>();
    private Set<Long> cbuCvuYaUsados = new HashSet<>();
    private Set<String> aliasYaUsados = new HashSet<>();
    // Atributos
    private String banco;
    private LocalDate fecha_de_alta;
    private Set<Cuenta> cuenta = new HashSet<>();

    // constructor
    public Cliente(String nombre_y_apellido, String direccion, Long telefono, String email, LocalDate fecha_de_nacimiento, Long dni, String banco, LocalDate fecha_de_alta){
        super(nombre_y_apellido, direccion, telefono, email, fecha_de_nacimiento, dni);
        this.banco = banco;
        this.fecha_de_alta = fecha_de_alta;
    }
    public Cliente(){}

    // Métodos
    public void getDatosCuenta(Long id){
        for (Cuenta c : this.cuenta) {
            if (c.getId_del_titular() == id) {
                c.getDatosCuenta();
            }
        }
    }
    public boolean estaIdUsado(String id){
        return this.idsYaUsados.contains(String.valueOf(id));
    }
    public boolean estaAliasUsado(String alias){
        return this.aliasYaUsados.contains(alias);
    }
    public boolean estaCbuCvuUsado(Long cbuCvu){
        return this.cbuCvuYaUsados.contains(cbuCvu);
    }

    // Getters y Setters
    public void agregarId(String id){
        this.idsYaUsados.add(id);
    }
    public void agregarAlias(String alias){
        this.aliasYaUsados.add(alias);
    }
    public void agregarCbuCvu(Long cbuCvu){
        this.cbuCvuYaUsados.add(cbuCvu);
    }
    public Cuenta buscarCuentaPorId(String id) throws AccountNotFoundException{
        for (Cuenta cuenta : this.cuenta) {
            if(cuenta.getId_del_titular().equals(id)){
                return cuenta;
            }
        }
        throw new AccountNotFoundException("No existe cuenta con id: " + id);
    }
    public Set<Cuenta> getCuentas() {
        return this.cuenta;
    }
    public void setCuentas(Cuenta cuenta){
        this.cuenta.add(cuenta);
    }
    public String getBanco() {
        return this.banco;
    }
    public void setBanco(String banco){
        this.banco = banco;
    }
    public LocalDate getFecha_de_alta() {
        return this.fecha_de_alta;
    }
    public void setFecha_de_alta(LocalDate fecha_de_alta){
        this.fecha_de_alta = fecha_de_alta;
    }
    // metodo para mostrar los datos de la cuenta
    public void mostrarCuentas(){
        for(Cuenta c : this.cuenta){
            c.getDatosCuenta();
        }
    }
}
