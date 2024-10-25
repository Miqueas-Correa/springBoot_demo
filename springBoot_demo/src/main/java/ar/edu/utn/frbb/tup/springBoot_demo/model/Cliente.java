package ar.edu.utn.frbb.tup.springBoot_demo.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.dto.ClienteDto;

public class Cliente extends Persona {
    // Atributos
    private String banco;
    private LocalDate fecha_de_alta;

    // constructor
    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getNombre_y_apellido(), clienteDto.getTelefono(), clienteDto.getEmail(), clienteDto.getFechaNacimiento(), clienteDto.getDni());
        fecha_de_alta = LocalDate.now();
        banco = clienteDto.getBanco();
    }

    // Getters y Setters
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

    // Método para parsear el clienete al archivo
    @Override
    public String toString() {
        return this.getDni().toString() + ";" + this.getNombre_y_apellido().toString() + ";" + this.getTelefono().toString() + ";" + this.getEmail().toString() + ";" + this.getFecha_de_nacimiento().toString() + ";" + this.banco + ";" + this.fecha_de_alta.toString();
    }
}