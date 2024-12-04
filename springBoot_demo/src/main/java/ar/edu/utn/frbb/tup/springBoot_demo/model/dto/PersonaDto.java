package ar.edu.utn.frbb.tup.springBoot_demo.model.dto;

import java.time.LocalDate;

public class PersonaDto {
    private String nombre_y_apellido;
    private long telefono;
    private String email;
    private long dni;
    private LocalDate fechaNacimiento;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public String getNombre_y_apellido() {
        return nombre_y_apellido;
    }

    public void setNombre_y_apellido(String nombre_y_apellido) {
        this.nombre_y_apellido = nombre_y_apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}