package ar.edu.utn.frbb.tup.springBoot_demo.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.Cuenta;

public class Cliente extends Persona {
    // Atributos
    private String banco;
    private LocalDate fecha_de_alta;

    // constructor
    public Cliente(String nombre_y_apellido, long telefono, String email, LocalDate fecha_de_nacimiento, Long dni, String banco, LocalDate fecha_de_alta){
        super(nombre_y_apellido, telefono, email, fecha_de_nacimiento, dni);
        this.banco = banco;
        this.fecha_de_alta = LocalDate.now();
    }
    public Cliente(Cuenta clienteDto) {
        super(clienteDto.getNombre_y_apellido(), clienteDto.getTelefono(), clienteDto.getEmail(), clienteDto.getFechaNacimiento(), clienteDto.getDni());
        fecha_de_alta = LocalDate.now();
        banco = clienteDto.getBanco();
    }
    public Cliente(){super();}

    // Método para parsear el clienete al archivo
    @Override
    public String toString() {
        return this.getNombre_y_apellido().toString() + ";" + this.getTelefono().toString() + ";" + this.getEmail().toString() + ";" + this.getFecha_de_nacimiento().toString() + ";" + this.getDni().toString() + ";" + this.banco + ";" + this.fecha_de_alta.toString();
    }

    // Getters y Setters
    // public Cuenta buscarCuentaPorId(String id) throws AccountNotFoundException{
    //     for (Cuenta cuenta : this.numeroCuenta) {
    //         if(cuenta.getTitular().equals(id)){
    //             return cuenta;
    //         }
    //     }
    //     throw new AccountNotFoundException("No se encuentra una cuenta asociada con el id: " + id);
    // }
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
}