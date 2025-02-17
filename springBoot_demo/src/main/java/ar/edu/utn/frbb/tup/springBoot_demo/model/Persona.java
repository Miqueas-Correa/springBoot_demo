package ar.edu.utn.frbb.tup.springBoot_demo.model;
import java.time.LocalDate;
import java.time.Period;

public class Persona {
    // Atributos
    private long dni;
    private String nombre_y_apellido;
    private long telefono;
    private String email;
    private LocalDate fecha_de_nacimiento;

    // constructor
    public Persona(String nombre_y_apellido, long telefono, String email, LocalDate fecha_de_nacimiento, Long dni){
        this.nombre_y_apellido = nombre_y_apellido;
        this.telefono = telefono;
        this.email = email;
        this.fecha_de_nacimiento = fecha_de_nacimiento;
        this.dni = dni;
    }
    public Persona(){}

    // Getters y Setters
    public String getNombre_y_apellido(){
        return this.nombre_y_apellido;
    }
    public void setNombre_y_apellido(String nombre_y_apellido){
        this.nombre_y_apellido = nombre_y_apellido;
    }
    public Long getTelefono(){
        return this.telefono;
    }
    public void setTelefono(Long telefono){
        this.telefono = telefono;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getFecha_de_nacimiento(){
        return this.fecha_de_nacimiento.toString();
    }
    public void setFecha_de_nacimiento(LocalDate fecha_de_nacimiento){
        this.fecha_de_nacimiento = fecha_de_nacimiento;
    }
    public Long getDni(){
        return this.dni;
    }
    public void setDni(Long dni){
        this.dni = dni;
    }

    // Método para calcular la edad
    public int getEdad() {
        if (this.fecha_de_nacimiento == null) throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        return Period.between(this.fecha_de_nacimiento, LocalDate.now()).getYears();
    }
}